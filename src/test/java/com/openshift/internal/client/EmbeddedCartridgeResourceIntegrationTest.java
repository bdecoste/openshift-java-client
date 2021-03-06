/******************************************************************************* 
 * Copyright (c) 2013 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package com.openshift.internal.client;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URISyntaxException;

import org.junit.Before;
import org.junit.Test;

import com.openshift.client.IApplication;
import com.openshift.client.ICartridge;
import com.openshift.client.IDomain;
import com.openshift.client.IEmbeddableCartridge;
import com.openshift.client.IUser;
import com.openshift.client.LatestVersionOf;
import com.openshift.client.OpenShiftEndpointException;
import com.openshift.client.OpenShiftException;
import com.openshift.client.utils.ApplicationAssert;
import com.openshift.client.utils.ApplicationTestUtils;
import com.openshift.client.utils.DomainTestUtils;
import com.openshift.client.utils.EmbeddedCartridgeAssert;
import com.openshift.client.utils.EmbeddedCartridgeTestUtils;
import com.openshift.client.utils.TestConnectionFactory;

/**
 * @author André Dietisheim
 */
public class EmbeddedCartridgeResourceIntegrationTest {

	private static final int WAIT_FOR_APPLICATION = 180 * 1000;

	private IDomain domain;
	private IUser user;

	@Before
	public void setUp() throws OpenShiftException, IOException {
		this.user = new TestConnectionFactory().getConnection().getUser();
		this.domain = DomainTestUtils.ensureHasDomain(user);
	}

	@Test
	public void shouldReturnEmbeddedCartridges() throws SocketTimeoutException, OpenShiftException {
		// pre-conditions
		IApplication application = ApplicationTestUtils.ensureHasExactly1Application(ICartridge.JBOSSAS_7, domain);

		// operation

		// verification
		assertTrue(application.getEmbeddedCartridges().size() >= 0);
	}

	@Test
	public void shouldEmbedMySQL() throws SocketTimeoutException, OpenShiftException, URISyntaxException {
		// pre-conditions
		IApplication application = ApplicationTestUtils.ensureHasExactly1Application(ICartridge.JBOSSAS_7, domain);
		EmbeddedCartridgeTestUtils.destroyAllEmbeddedCartridges(application);
		IEmbeddableCartridge mysql = EmbeddedCartridgeTestUtils.getFirstEmbeddableCartridge(
				LatestVersionOf.mySQL(), user.getConnection());
		assertThat(new ApplicationAssert(application))
			.hasNotEmbeddableCartridge(mysql);
		int numOfEmbeddedCartridges = application.getEmbeddedCartridges().size();

		// operation
		application.addEmbeddableCartridge(LatestVersionOf.mySQL());

		// verification
		assertThat(new ApplicationAssert(application))
				.hasEmbeddableCartridges(numOfEmbeddedCartridges + 1)
				.hasEmbeddedCartridges(LatestVersionOf.mySQL());

		new EmbeddedCartridgeAssert(application.getEmbeddedCartridge(mysql))
				.hasUrl();
	}

	/**
	 * Verify in application created with different user instance.
	 */
	@Test
	public void shouldHaveUrlInEmbeddedMySQL() throws OpenShiftException, URISyntaxException, FileNotFoundException,
			IOException {
		// pre-conditions
		IApplication application = ApplicationTestUtils.ensureHasExactly1Application(ICartridge.JBOSSAS_7, domain);
		EmbeddedCartridgeTestUtils.ensureHasEmbeddedCartridges(LatestVersionOf.mySQL(), application);
		// verify using user instance that's not the one used to create
		IUser user2 = new TestConnectionFactory().getConnection().getUser();
		IApplication user2Application = user2.getDefaultDomain().getApplicationByName(application.getName());
		assertThat(new ApplicationAssert(user2Application))
				.hasEmbeddedCartridges(LatestVersionOf.mySQL());

		// operation

		// verification
		IEmbeddableCartridge mysql =
				EmbeddedCartridgeTestUtils.getFirstEmbeddableCartridge(LatestVersionOf.mySQL(), user2.getConnection());
		new EmbeddedCartridgeAssert(user2Application.getEmbeddedCartridge(mysql))
				.hasUrl();
	}

	@Test
	public void shouldReturnThatHasMySQL() throws OpenShiftException, FileNotFoundException, IOException {
		// pre-conditions
		IApplication application = ApplicationTestUtils.ensureHasExactly1Application(ICartridge.JBOSSAS_7, domain);
		EmbeddedCartridgeTestUtils.ensureHasEmbeddedCartridges(LatestVersionOf.mySQL(), application);
		// verify using user instance that's not the one used to create
		IUser user2 = new TestConnectionFactory().getConnection().getUser();
		IApplication user2Application = user2.getDefaultDomain().getApplicationByName(application.getName());
		EmbeddedCartridgeTestUtils.ensureHasEmbeddedCartridges(LatestVersionOf.mySQL(), user2Application);

		// operation

		// verification
		assertThat(new ApplicationAssert(user2Application))
				.hasEmbeddedCartridges(LatestVersionOf.mySQL());
	}

	@Test
	public void shouldEmbedPostgreSQL() throws SocketTimeoutException, OpenShiftException, URISyntaxException {
		// pre-conditions
		IApplication application = ApplicationTestUtils.ensureHasExactly1Application(ICartridge.JBOSSAS_7, domain);
		EmbeddedCartridgeTestUtils.destroyAllEmbeddedCartridges(application);
		IEmbeddableCartridge postgres = EmbeddedCartridgeTestUtils.getFirstEmbeddableCartridge(
				LatestVersionOf.postgreSQL(), user.getConnection());
		assertThat(new ApplicationAssert(application))
			.hasNotEmbeddableCartridge(postgres);

		// operation
		application.addEmbeddableCartridge(LatestVersionOf.postgreSQL());

		// verification
		assertThat(new ApplicationAssert(application))
				.hasEmbeddedCartridges(LatestVersionOf.postgreSQL());

		new EmbeddedCartridgeAssert(application.getEmbeddedCartridge(postgres))
				.hasUrl();
	}

	@Test
	public void shouldHaveUrlInEmbeddedPostgres() throws OpenShiftException, URISyntaxException, FileNotFoundException,
			IOException {
		// pre-conditions
		IApplication application = ApplicationTestUtils.ensureHasExactly1Application(ICartridge.JBOSSAS_7, domain);
		EmbeddedCartridgeTestUtils.ensureHasEmbeddedCartridges(LatestVersionOf.postgreSQL(), application);
		// verify using user instance that's not the one used to create
		IUser user2 = new TestConnectionFactory().getConnection().getUser();
		IApplication user2Application = user2.getDefaultDomain().getApplicationByName(application.getName());
		assertThat(new ApplicationAssert(user2Application)).hasEmbeddedCartridges(LatestVersionOf.postgreSQL());

		// operation

		// verification
		IEmbeddableCartridge postgres =
				EmbeddedCartridgeTestUtils.getFirstEmbeddableCartridge(LatestVersionOf.postgreSQL(),
						user2.getConnection());
		new EmbeddedCartridgeAssert(user2Application.getEmbeddedCartridge(postgres))
				.hasUrl();
	}

	@Test
	public void shouldEmbedMongo() throws Exception {
		// pre-conditions
		IApplication application = ApplicationTestUtils.ensureHasExactly1Application(ICartridge.JBOSSAS_7, domain);
		EmbeddedCartridgeTestUtils.destroyAllEmbeddedCartridges(application);
		IEmbeddableCartridge mongo = EmbeddedCartridgeTestUtils.getFirstEmbeddableCartridge(
				LatestVersionOf.mongoDB(), user.getConnection());
		assertThat(new ApplicationAssert(application))
			.hasNotEmbeddableCartridge(mongo);

		// operation
		application.addEmbeddableCartridge(LatestVersionOf.mongoDB());

		// verification
		assertThat(new ApplicationAssert(application)
				.hasEmbeddedCartridges(LatestVersionOf.mongoDB()));
		new EmbeddedCartridgeAssert(application.getEmbeddedCartridge(mongo))
				.hasUrl();
	}

	@Test
	public void shouldHaveUrlInEmbeddedMongo() throws OpenShiftException, URISyntaxException, FileNotFoundException,
			IOException {
		// pre-conditions
		IApplication application = ApplicationTestUtils.ensureHasExactly1Application(ICartridge.JBOSSAS_7, domain);
		EmbeddedCartridgeTestUtils.ensureHasEmbeddedCartridges(LatestVersionOf.mongoDB(), application);
		// verify using user instance that's not the one used to create
		IUser user2 = new TestConnectionFactory().getConnection().getUser();
		IApplication user2Application = user2.getDefaultDomain().getApplicationByName(application.getName());
		assertThat(new ApplicationAssert(user2Application)).hasEmbeddedCartridges(LatestVersionOf.mongoDB());

		// operation

		// verification
		IEmbeddableCartridge mongo =
				EmbeddedCartridgeTestUtils
						.getFirstEmbeddableCartridge(LatestVersionOf.mongoDB(), user2.getConnection());
		new EmbeddedCartridgeAssert(user2Application.getEmbeddedCartridge(mongo))
				.hasUrl();
	}

	@Test
	public void shouldEmbedRockMongo() throws Exception {
		// pre-conditions
		// have to make sure have non-scalable app without cartridges
		ApplicationTestUtils.silentlyDestroyAllApplications(domain);
		IApplication application = ApplicationTestUtils.createApplication(ICartridge.JBOSSAS_7, domain);
		assertThat(new ApplicationAssert(application)
				.hasNotEmbeddableCartridges(LatestVersionOf.mongoDB())
				.hasNotEmbeddableCartridges(LatestVersionOf.rockMongo()));

		// operation
		application.addEmbeddableCartridge(LatestVersionOf.mongoDB());
		application.addEmbeddableCartridge(LatestVersionOf.rockMongo());

		// verification
		assertThat(new ApplicationAssert(application)
				.hasEmbeddedCartridges(LatestVersionOf.mongoDB())
				.hasEmbeddedCartridges(LatestVersionOf.rockMongo()));
		IEmbeddableCartridge rockMongo =
				EmbeddedCartridgeTestUtils.getFirstEmbeddableCartridge(LatestVersionOf.rockMongo(),
						user.getConnection());
		new EmbeddedCartridgeAssert(application.getEmbeddedCartridge(rockMongo))
				.hasUrl();
	}

	@Test
	public void shouldHaveUrlInEmbeddedRockMongo() throws SocketTimeoutException, OpenShiftException,
			URISyntaxException {
		// pre-conditions
		IApplication application = ApplicationTestUtils.ensureHasExactly1Application(ICartridge.JBOSSAS_7, domain);
		assertThat(new ApplicationAssert(application)).hasEmbeddedCartridges(LatestVersionOf.rockMongo());

		// operation

		// verification
		IEmbeddableCartridge rockMongo =
				EmbeddedCartridgeTestUtils.getFirstEmbeddableCartridge(LatestVersionOf.rockMongo(),
						user.getConnection());
		new EmbeddedCartridgeAssert(application.getEmbeddedCartridge(rockMongo))
				.hasUrl();
	}

	@Test
	public void shouldEmbedPhpMyAdmin() throws Exception {
		// pre-conditions
		IApplication application = ApplicationTestUtils.ensureHasExactly1Application(ICartridge.JBOSSAS_7, domain);
		EmbeddedCartridgeTestUtils.destroyAllEmbeddedCartridges(application);
		assertThat(new ApplicationAssert(application)
				.hasNotEmbeddableCartridges(LatestVersionOf.mySQL())
				.hasNotEmbeddableCartridges(LatestVersionOf.phpMyAdmin()));

		// operation
		application.addEmbeddableCartridge(LatestVersionOf.mySQL());
		application.addEmbeddableCartridge(LatestVersionOf.phpMyAdmin());

		// verification
		assertThat(new ApplicationAssert(application)
				.hasEmbeddedCartridges(LatestVersionOf.mySQL()))
				.hasEmbeddedCartridges(LatestVersionOf.phpMyAdmin());
		IEmbeddableCartridge phpMyAdmin =
				EmbeddedCartridgeTestUtils.getFirstEmbeddableCartridge(LatestVersionOf.phpMyAdmin(),
						user.getConnection());
		new EmbeddedCartridgeAssert(application.getEmbeddedCartridge(phpMyAdmin))
				.hasUrl();
	}

	@Test
	public void shouldHaveUrlInEmbeddedPhpMyadmin()
			throws OpenShiftException, URISyntaxException, FileNotFoundException, IOException {
		// pre-conditions
		IApplication application = ApplicationTestUtils.ensureHasExactly1Application(ICartridge.JBOSSAS_7, domain);
		assertThat(new ApplicationAssert(application)).hasEmbeddedCartridges(LatestVersionOf.phpMyAdmin());

		// operation

		// verification
		// verify using user instance that's not the one used to create
		IUser user2 = new TestConnectionFactory().getConnection().getUser();
		IApplication user2Application = user2.getDefaultDomain().getApplicationByName(application.getName());
		IEmbeddableCartridge phpMyAdmin =
				EmbeddedCartridgeTestUtils.getFirstEmbeddableCartridge(LatestVersionOf.phpMyAdmin(),
						user2.getConnection());
		new EmbeddedCartridgeAssert(user2Application.getEmbeddedCartridge(phpMyAdmin))
				.hasUrl();
	}

	@Test
	public void shouldEmbedJenkinsClient() throws Exception {
		// pre-conditions
		// need 2 free gears; jenkins + builder
		IApplication application = ApplicationTestUtils.ensureHasExactly1Application(ICartridge.JBOSSAS_7, domain);
		EmbeddedCartridgeTestUtils.destroyAllEmbeddedCartridges(application);
		IApplication jenkins = ApplicationTestUtils.createApplication(ICartridge.JENKINS_14, domain);
		assertTrue(jenkins.waitForAccessible(WAIT_FOR_APPLICATION));

		// operation
		application.addEmbeddableCartridge(LatestVersionOf.jenkinsClient());

		// verification
		assertThat(new ApplicationAssert(application)
				.hasEmbeddedCartridges(LatestVersionOf.jenkinsClient()));
		IEmbeddableCartridge jenkinsClient =
				EmbeddedCartridgeTestUtils.getFirstEmbeddableCartridge(
						LatestVersionOf.jenkinsClient(), user.getConnection());
		new EmbeddedCartridgeAssert(application.getEmbeddedCartridge(jenkinsClient))
				.hasUrl();
	}

	/**
	 * Verify in application created with different user instance.
	 * 
	 * @throws SocketTimeoutException
	 * @throws OpenShiftException
	 * @throws URISyntaxException
	 */
	@Test
	public void shouldHaveUrlInEmbeddedJenkinsClient() throws OpenShiftException, URISyntaxException,
			FileNotFoundException, IOException {
		// pre-conditions
		IApplication jenkinsApplication = ApplicationTestUtils.getOrCreateApplication(domain, ICartridge.JENKINS_14);
		assertThat(jenkinsApplication).isNotNull();
		EmbeddedCartridgeTestUtils.ensureHasEmbeddedCartridges(LatestVersionOf.jenkinsClient(), jenkinsApplication);
		assertThat(new ApplicationAssert(jenkinsApplication)).hasEmbeddedCartridges(LatestVersionOf.jenkinsClient());

		// operation

		// verification
		// verify using user instance that's not the one used to create
		IUser user2 = new TestConnectionFactory().getConnection().getUser();
		IApplication user2Application = user2.getDefaultDomain().getApplicationByName(jenkinsApplication.getName());
		IEmbeddableCartridge jenkinsClient =
				EmbeddedCartridgeTestUtils.getFirstEmbeddableCartridge(LatestVersionOf.jenkinsClient(),
						user2.getConnection());
		new EmbeddedCartridgeAssert(user2Application.getEmbeddedCartridge(jenkinsClient))
				.hasUrl();
	}

	@Test(expected = OpenShiftEndpointException.class)
	public void shouldNotAddEmbeddedCartridgeTwice() throws Exception {
		// pre-conditions
		IApplication application = ApplicationTestUtils.ensureHasExactly1Application(ICartridge.JBOSSAS_7, domain);
		EmbeddedCartridgeTestUtils.ensureHasEmbeddedCartridges(LatestVersionOf.mySQL(), application);

		// operation
		application.addEmbeddableCartridge(LatestVersionOf.mySQL());
	}

	@Test
	public void shouldRemoveEmbeddedCartridge() throws Exception {
		// pre-conditions
		IApplication application = ApplicationTestUtils.ensureHasExactly1Application(ICartridge.JBOSSAS_7, domain);
		EmbeddedCartridgeTestUtils.ensureHasEmbeddedCartridges(LatestVersionOf.mySQL(), application);
		int numOfEmbeddedCartridges = application.getEmbeddedCartridges().size();

		// operation
		application.removeEmbeddedCartridges(LatestVersionOf.mySQL());

		// verification
		assertTrue(application.getEmbeddedCartridges().size() == numOfEmbeddedCartridges - 1);
		assertThat(new ApplicationAssert(application))
				.hasNotEmbeddableCartridge(EmbeddedCartridgeTestUtils.getLatestMySqlCartridge(user.getConnection()));
	}

	@Test
	public void shouldNotRemoveEmbeddedCartridgeThatWasNotAdded() throws SocketTimeoutException, OpenShiftException {
		// pre-conditions
		IApplication application = ApplicationTestUtils.ensureHasExactly1Application(ICartridge.JBOSSAS_7, domain);
		EmbeddedCartridgeTestUtils.silentlyDestroy(LatestVersionOf.mySQL(), application);
		int numOfEmbeddedCartridges = application.getEmbeddedCartridges().size();

		// operation
		application.removeEmbeddedCartridges(LatestVersionOf.mySQL());

		// verification
		IEmbeddableCartridge mySql = EmbeddedCartridgeTestUtils.getLatestMySqlCartridge(user.getConnection());
		assertThat(new ApplicationAssert(application))
				.hasEmbeddableCartridges(numOfEmbeddedCartridges)
				.hasNotEmbeddableCartridge(mySql.getName());
	}

	@Test
	public void shouldSeeCartridgeRemovedWithOtherUser() throws Exception {
		// pre-condition
		IApplication application = ApplicationTestUtils.ensureHasExactly1Application(ICartridge.JBOSSAS_7, domain);
		IEmbeddableCartridge mySqlEmbeddableCartridge =
				EmbeddedCartridgeTestUtils.getLatestMySqlCartridge(user.getConnection());
		EmbeddedCartridgeTestUtils.ensureHasEmbeddedCartridge(mySqlEmbeddableCartridge, application);
		assertThat(new ApplicationAssert(application)
				.hasEmbeddedCartridges(LatestVersionOf.mySQL()));

		// operation
		// use user instance that's not the one used to create
		IUser user2 = new TestConnectionFactory().getConnection().getUser();
		IApplication user2Application = user2.getDefaultDomain().getApplicationByName(application.getName());
		user2Application.removeEmbeddedCartridges(LatestVersionOf.mySQL());
		assertThat(new ApplicationAssert(user2Application)
				.hasNotEmbeddableCartridges(LatestVersionOf.mySQL()));

		// verification
		application.refresh();
		assertThat(new ApplicationAssert(application)
				.hasNotEmbeddableCartridges(LatestVersionOf.mySQL()));
		assertEquals(application.getEmbeddedCartridges().size(), user2Application.getEmbeddedCartridges().size());
	}
}
