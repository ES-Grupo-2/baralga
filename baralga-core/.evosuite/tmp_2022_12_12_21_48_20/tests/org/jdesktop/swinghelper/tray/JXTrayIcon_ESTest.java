/*
 * This file was automatically generated by EvoSuite
 * Tue Dec 13 00:59:17 GMT 2022
 */

package org.jdesktop.swinghelper.tray;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class) @EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true, useJEE = true) 
public class JXTrayIcon_ESTest extends JXTrayIcon_ESTest_scaffolding {

  @Test(timeout = 4000)
  public void test0()  throws Throwable  {
      String string0 = "org.jdesktop.swinghelper.tray.JXTrayIcon";
      Thread thread0 = Thread.currentThread();
      ClassLoader classLoader0 = thread0.getContextClassLoader();
      boolean boolean0 = true;
      // Undeclared exception!
      try { 
        Class.forName(string0, boolean0, classLoader0);
        fail("Expecting exception: NoClassDefFoundError");
      
      } catch(NoClassDefFoundError e) {
      }
  }
}
