/******************************************************************************* 
 * Copyright (c) 2007 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package com.openshift.client;

import static org.fest.assertions.Assertions.assertThat;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import com.jcraft.jsch.UserInfo;
import com.openshift.client.utils.ApplicationAssert;
import com.openshift.client.utils.ApplicationTestUtils;
import com.openshift.client.utils.DomainTestUtils;
import com.openshift.client.utils.OpenShiftTestConfiguration;
import com.openshift.internal.client.ApplicationInfo;

/**
 * @author André Dietisheim
 */
public class ApplicationResourceIntegrationTest {

	private static IDomain domain;

	@Before
	public void setUp() throws FileNotFoundException, IOException, OpenShiftException {
		final OpenShiftTestConfiguration configuration = new OpenShiftTestConfiguration();
		final IOpenShiftConnection connection =
				new OpenShiftConnectionFactory().create(
						configuration.getClientId(),
						configuration.getRhlogin(),
						configuration.getPassword(),
						configuration.getLibraServer());
		IUser user = connection.getUser();
		domain = DomainTestUtils.getFirstDomainOrCreate(user);
	}

	@AfterClass
	public static void cleanUp() {
		ApplicationTestUtils.silentlyDestroyAllApplications(domain);
	}

	@Test
	public void shouldCreateApplication() throws Exception {
		String applicationName =
				ApplicationTestUtils.createRandomApplicationName();
		IApplication application = null;
		application = domain.createApplication(
				applicationName, ICartridge.JBOSSAS_7, null, null);
		assertThat(new ApplicationAssert(application))
				.hasName(applicationName)
				.hasUUID()
				.hasCartridge(ICartridge.JBOSSAS_7)
				.hasValidApplicationUrl()
				.hasValidGitUrl()
				.hasEmbeddableCartridges()
				.hasAlias();
	}

	@Test
	public void canCreateRubyApplication() throws Exception {
		// String applicationName =
		// ApplicationTestUtils.createRandomApplicationName();
		// IRubyApplication application = null;
		// try {
		// application = service.createRubyApplication(applicationName, user);
		// assertNotNull(application);
		// assertEquals(applicationName, application.getName());
		// assertTrue(application.getCartridge() instanceof RubyCartridge);
		// } catch (Exception e) {
		// e.printStackTrace();
		// throw e;
		// } finally {
		// ApplicationTestUtils.silentlyDestroyApplication(applicationName,
		// application.getCartridge(), user, service);
		// }
	}

	@Test
	public void canCreateHAProxyApplication() throws Exception {
		// String applicationName =
		// ApplicationTestUtils.createRandomApplicationName();
		// IHAProxyApplication application = null;
		// try {
		// application = service.createHAProxyApplication(applicationName,
		// user);
		// assertNotNull(application);
		// assertEquals(applicationName, application.getName());
		// assertTrue(application.getCartridge() instanceof HAProxyCartridge);
		// } catch (Exception e) {
		// e.printStackTrace();
		// throw e;
		// } finally {
		// ApplicationTestUtils.silentlyDestroyApplication(applicationName,
		// application.getCartridge(), user, service);
		// }
	}

	@Test
	public void canRawProxyApplication() throws Exception {
		// String applicationName =
		// ApplicationTestUtils.createRandomApplicationName();
		// IRawApplication application = null;
		// try {
		// application = service.createRawApplication(applicationName, user);
		// assertNotNull(application);
		// assertEquals(applicationName, application.getName());
		// assertTrue(application.getCartridge() instanceof RawCartridge);
		// } catch (Exception e) {
		// e.printStackTrace();
		// throw e;
		// } finally {
		// ApplicationTestUtils.silentlyDestroyApplication(applicationName,
		// application.getCartridge(), user, service);
		// }
	}

	@Test
	public void canCreatePythonApplication() throws Exception {
		// String applicationName =
		// ApplicationTestUtils.createRandomApplicationName();
		// IPythonApplication application = null;
		// try {
		// application = service.createPythonApplication(applicationName, user);
		// assertNotNull(application);
		// assertEquals(applicationName, application.getName());
		// assertTrue(application.getCartridge() instanceof PythonCartridge);
		// } catch (Exception e) {
		// e.printStackTrace();
		// throw e;
		// } finally {
		// ApplicationTestUtils.silentlyDestroyApplication(applicationName,
		// application.getCartridge(), user, service);
		// }
	}

	@Test
	public void canCreatePHPApplication() throws Exception {
		// String applicationName =
		// ApplicationTestUtils.createRandomApplicationName();
		// IPHPApplication application = null;
		// try {
		// application = service.createPHPApplication(applicationName, user);
		// assertNotNull(application);
		// assertEquals(applicationName, application.getName());
		// assertTrue(application.getCartridge() instanceof PHPCartridge);
		// } catch (Exception e) {
		// e.printStackTrace();
		// throw e;
		// } finally {
		// ApplicationTestUtils.silentlyDestroyApplication(applicationName,
		// application.getCartridge(), user, service);
		// }
	}

	@Test
	public void canCreatePerlApplication() throws Exception {
		// String applicationName =
		// ApplicationTestUtils.createRandomApplicationName();
		// IPerlApplication application = null;
		// try {
		// application = service.createPerlApplication(applicationName, user);
		// assertNotNull(application);
		// assertEquals(applicationName, application.getName());
		// assertTrue(application.getCartridge() instanceof PerlCartridge);
		// } catch (Exception e) {
		// e.printStackTrace();
		// throw e;
		// } finally {
		// ApplicationTestUtils.silentlyDestroyApplication(applicationName,
		// application.getCartridge(), user, service);
		// }
	}

	@Test
	public void canCreateNodeJSApplication() throws Exception {
		// String applicationName =
		// ApplicationTestUtils.createRandomApplicationName();
		// INodeJSApplication application = null;
		// try {
		// application = service.createNodeJSApplication(applicationName, user);
		// assertNotNull(application);
		// assertEquals(applicationName, application.getName());
		// assertTrue(application.getCartridge() instanceof NodeJSCartridge);
		// } catch (Exception e) {
		// e.printStackTrace();
		// throw e;
		// } finally {
		// ApplicationTestUtils.silentlyDestroyApplication(applicationName,
		// application.getCartridge(), user, service);
		// }
	}

	@Test
	public void canCreateJenkinsApplication() throws Exception {
		// String applicationName =
		// ApplicationTestUtils.createRandomApplicationName();
		// IJenkinsApplication application = null;
		// try {
		// application = service.createJenkinsApplication(applicationName,
		// user);
		// assertNotNull(application);
		// assertEquals(applicationName, application.getName());
		// assertTrue(application.getCartridge() instanceof JenkinsCartridge);
		// } catch (Exception e) {
		// e.printStackTrace();
		// throw e;
		// } finally {
		// ApplicationTestUtils.silentlyDestroyApplication(applicationName,
		// application.getCartridge(), user, service);
		// }
	}

	@Test
	public void shouldDestroyApplication() throws Exception {
		// pre-condition
		IApplication application = ApplicationTestUtils.getOrCreateApplication(domain);
		assertThat(application.getName()).isNotEmpty();

		// operation
		application.destroy();

		// verification
		assertThat(domain.hasApplicationByName(application.getName())).isFalse();
	}

	@Test(expected = OpenShiftException.class)
	public void createDuplicateApplicationThrowsException() throws Exception {
		IApplication application2 = null;
		try {
			// pre-condition
			IApplication application = ApplicationTestUtils.getOrCreateApplication(domain);
			assertThat(application.getName()).isNotEmpty();

			// operation
			application2 = domain.createApplication(application.getName(), ICartridge.JBOSSAS_7, null, null);
		} finally {
			ApplicationTestUtils.silentlyDestroy(application2);
		}
	}

	@Test
	public void canStopApplication() throws Exception {
		// pre-condition
		IApplication application = ApplicationTestUtils.getOrCreateApplication(domain);

		// operation
		application.stop();
	}

	@Test
	public void canStartStoppedApplication() throws Exception {
		// pre-condition
		IApplication application = ApplicationTestUtils.getOrCreateApplication(domain);
		application.stop();

		// operation
		application.start();
	}

	@Test
	public void canStartStartedApplication() throws Exception {
		// pre-condition
		IApplication application = ApplicationTestUtils.getOrCreateApplication(domain);
		application.start();

		// operation
		application.start();
	}

	@Test
	public void canStopStoppedApplication() throws Exception {
		// pre-condition
		IApplication application = ApplicationTestUtils.getOrCreateApplication(domain);
		application.stop();

		// operation
		application.stop();
	}

	@Test
	public void canRestartApplication() throws Exception {
		// pre-condition
		IApplication application = ApplicationTestUtils.getOrCreateApplication(domain);
		application.start();

		// operation
		application.restart();
	}

	@Test
	public void canGetStatus() throws Exception {
		// String applicationName =
		// ApplicationTestUtils.createRandomApplicationName();
		// try {
		// IApplication application = service.createApplication(applicationName,
		// ICartridge.JBOSSAS_7, user);
		// String applicationStatus = service.getStatus(application.getName(),
		// application.getCartridge(), user);
		// assertNotNull(applicationStatus);
		// } finally {
		// ApplicationTestUtils.silentlyDestroyAS7Application(applicationName,
		// user, service);
		// }
	}

	@Test
	public void returnsValidApplicationUrl() throws Exception {
		// String applicationName =
		// ApplicationTestUtils.createRandomApplicationName();
		// try {
		// IApplication application = service.createApplication(applicationName,
		// ICartridge.JBOSSAS_7, user);
		// String applicationUrl = application.getApplicationUrl();
		// assertNotNull(applicationUrl);
		// assertApplicationUrl(applicationName, applicationUrl);
		// } finally {
		// ApplicationTestUtils.silentlyDestroyAS7Application(applicationName,
		// user, service);
		// }
	}

	@Test
	public void returnsCreationTime() throws Exception {
		// String applicationName =
		// ApplicationTestUtils.createRandomApplicationName();
		// try {
		// IApplication application = service.createApplication(applicationName,
		// ICartridge.JBOSSAS_7, user);
		// Date creationTime = application.getCreationTime();
		// assertNotNull(creationTime);
		// assertTrue(creationTime.compareTo(new Date()) == -1);
		// } finally {
		// ApplicationTestUtils.silentlyDestroyAS7Application(applicationName,
		// user, service);
		// }
	}

	/**
	 * This tests checks if the creation time is returned in the 2nd
	 * application. The creation time is only available in the
	 * {@link ApplicationInfo} which is held by the {@link UserInfo}. The
	 * UserInfo is fetched when the 1st application is created and then stored.
	 * The 2nd application has therefore to force the user to refresh the user
	 * info.
	 * 
	 * @throws Exception
	 * 
	 * @see UserInfo
	 * @see ApplicationInfo
	 */
	@Test
	public void returnsCreationTimeOn2ndApplication() throws Exception {
		// String applicationName = null;
		// String applicationName2 = null;
		// try {
		// applicationName = ApplicationTestUtils.createRandomApplicationName();
		// IApplication application = user.createApplication(applicationName,
		// ICartridge.JBOSSAS_7);
		// Date creationTime = application.getCreationTime();
		// assertNotNull(creationTime);
		// applicationName2 =
		// ApplicationTestUtils.createRandomApplicationName();
		// IApplication application2 = user.createApplication(applicationName2,
		// ICartridge.JBOSSAS_7);
		// Date creationTime2 = application2.getCreationTime();
		// assertNotNull(creationTime2);
		// assertTrue(creationTime.compareTo(creationTime2) == -1);
		// } finally {
		// ApplicationTestUtils.silentlyDestroyAS7Application(applicationName,
		// user, service);
		// ApplicationTestUtils.silentlyDestroyAS7Application(applicationName2,
		// user, service);
		// }
	}

	@Test
	public void canThreadDumpJBossApplication() throws Exception {
		// String applicationName =
		// ApplicationTestUtils.createRandomApplicationName();
		// ApplicationLogReader reader = null;
		// IJBossASApplication application = null;
		// try {
		// application = service.createJBossASApplication(applicationName,
		// user);
		// assertNotNull(application);
		// assertEquals(applicationName, application.getName());
		// assertTrue(application.getCartridge() instanceof JBossCartridge);
		//
		// String logFile = application.threadDump();
		//
		// String log = service.getStatus(applicationName,
		// application.getCartridge(), user, logFile, 100);
		//
		// assertTrue("Failed to retrieve logged thread dump",
		// log.contains("new generation"));
		//
		// } catch (Exception e) {
		// e.printStackTrace();
		// throw e;
		// } finally {
		// ApplicationTestUtils.silentlyDestroyApplication(applicationName,
		// application.getCartridge(), user, service);
		//
		// if (reader != null)
		// reader.close();
		// }
	}

	@Test
	public void canThreadDumpRackApplication() throws Exception {
		// String applicationName =
		// ApplicationTestUtils.createRandomApplicationName();
		// ApplicationLogReader reader = null;
		// InputStream urlStream = null;
		// IRubyApplication application = null;
		// try {
		// application = service.createRubyApplication(applicationName, user);
		// assertNotNull(application);
		// assertEquals(applicationName, application.getName());
		// assertTrue(application.getCartridge() instanceof RubyCartridge);
		//
		// URL url = new URL("http://" + applicationName + "-" +
		// user.getDomain().getNamespace() + ".dev.rhcloud.com/lobster");
		//
		// Thread.sleep(20 * 1000);
		//
		// HttpURLConnection connection = (HttpURLConnection)
		// url.openConnection();
		//
		// //Need to hit the app to start the Rack/Ruby process
		// String result =
		// StreamUtils.readToString(connection.getInputStream());
		//
		// String logFile = application.threadDump();
		//
		// logFile = "logs/error_log-20120229-000000-EST";
		//
		// String log = service.getStatus(applicationName,
		// application.getCartridge(), user, logFile, 100);
		//
		// assertTrue("Failed to retrieve logged thread dump",
		// log.contains("passenger-3.0.4"));
		//
		// } catch (Exception e) {
		// e.printStackTrace();
		// throw e;
		// } finally {
		// ApplicationTestUtils.silentlyDestroyApplication(applicationName,
		// application.getCartridge(), user, service);
		//
		// if (reader != null)
		// reader.close();
		//
		// if (urlStream != null)
		// urlStream.close();
		// }
	}

	@Test
	public void canWaitForApplication() throws OpenShiftException, MalformedURLException, IOException {
		// String applicationName = null;
		// IApplication application = null;
		// try {
		// applicationName = ApplicationTestUtils.createRandomApplicationName();
		// application = service.createJBossASApplication(applicationName,
		// user);
		// assertNotNull(application);
		//
		// assertTrue(application.waitForAccessible(WAIT_FOR_APPLICATION));
		//
		// } finally {
		// ApplicationTestUtils.silentlyDestroyApplication(applicationName,
		// application.getCartridge(), user, service);
		// }
	}
}
