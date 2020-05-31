package com.atguigu.gmall0423.passport;

import com.atguigu.gmall0423.passport.config.JwtUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GmallPassportWebApplicationTests {

	@Test
	public void contextLoads() {
	}

	@Test
	public void testJWT(){
		String key = "atguigu";
		HashMap<String , Object> map = new HashMap<>();
		map.put("userId",1001);
		map.put("nickName","admin");
		String salt = "192.168.25.220";
		String token = JwtUtil.encode(key, map, salt);

		System.out.println("token:"+token);
		// token=eyJhbGciOiJIUzI1NiJ9.eyJuaWNrTmFtZSI6ImFkbWluIiwidXNlcklkIjoxMDAxfQ.u5iNOJRMkJpzgl-zmwps86KmAGrTlY1dIzRFRWtgc5w

		// 解密token
		Map<String, Object> maps = JwtUtil.decode(token, key, "192.168.25.212");
		System.out.println(maps);

	}

}
