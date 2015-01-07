package com.github.ebnew.ki4so.client;

import junit.framework.Assert;
import org.junit.Test;

import com.github.ebnew.ki4so.client.key.DefaultKeyServiceImpl;

public class DefaultKeyServiceImplTest {

	DefaultKeyServiceImpl keyService = new DefaultKeyServiceImpl
				("http://localhost:8080/ki4so-web/fetchKey.do","1001");
	
	@Test
	public void testCheckKeyFileExistByToken(){
		Assert.assertEquals(true, keyService.checkKeyFileExistByToken("1001"));
	}
	@Test
	public void testGenerateKeyFile(){
		try {
			Assert.assertEquals(true, keyService.generateKeyFile("1001"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void testDecryptKey(){
		Assert.assertEquals("恭喜发财!",
				keyService.decryptKey("1001","3uMfvBYJLCgQN3g79tSQ1KtnbpIM5Nadxme/MUk9H7mXuXWXP1sg2NNbEm9pEnddyZ96bGjVHOKnqHHQwuTvHsf0hkLuY4bfKagv/euMTqHTNu+GAbSv4RLAtGSTfiznUZQdG3NAdI4pY//IoKLQJ3l7nVP3ZX0fNGtPTwOsPdg=")
		);
	}
	
}
