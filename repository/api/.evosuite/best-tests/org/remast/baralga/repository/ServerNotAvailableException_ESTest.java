/*
 * This file was automatically generated by EvoSuite
 * Sat Dec 17 12:38:05 GMT 2022
 */

package org.remast.baralga.repository;

import org.junit.Test;
import static org.junit.Assert.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;
import org.remast.baralga.repository.ServerNotAvailableException;

@RunWith(EvoRunner.class) @EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true, useJEE = true) 
public class ServerNotAvailableException_ESTest extends ServerNotAvailableException_ESTest_scaffolding {

  @Test(timeout = 4000)
  public void test0()  throws Throwable  {
      ServerNotAvailableException serverNotAvailableException0 = new ServerNotAvailableException((String) null);
      String string0 = serverNotAvailableException0.getBaseUrl();
      assertNull(string0);
  }

  @Test(timeout = 4000)
  public void test1()  throws Throwable  {
      ServerNotAvailableException serverNotAvailableException0 = new ServerNotAvailableException("22Q");
      String string0 = serverNotAvailableException0.getBaseUrl();
      assertEquals("22Q", string0);
  }

  @Test(timeout = 4000)
  public void test2()  throws Throwable  {
      ServerNotAvailableException serverNotAvailableException0 = new ServerNotAvailableException("");
      String string0 = serverNotAvailableException0.getBaseUrl();
      assertEquals("", string0);
  }
}
