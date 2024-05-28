package com.example.demo.fliter;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import com.example.demo.WebKeyUtil;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebFilter("/form_auth/*")
public class LoginFilter implements Filter {
	
	static Map<String, Map<String, String>> users = new HashMap<>();
	static{
		users.put("user", Map.of("hash", "7723786a93d89c62a3f875d92daf585550fe3669ff4b997d7730bb39d53d20c9", 
				"salt", "ab6ee905e4f60232c73fb8323270948f")); // 1234
		users.put("John", Map.of("hash", "e19a311a6ff13fe3c3dad73a37f3243eb8be7c2e9d15df77df2ef8095ff41e0f", 
				"salt", "6768fa0bffa5fa914736932e2e293014")); // 1234
		users.put("Mary", Map.of("hash", "33cddbb9d7386658408cbe23c1f01a6c5d7398e019da1658228db819d71d7e93", 
				"salt", "2591b7b69f6bd8e7d375eaaa3d98d8b9")); // 5678
		users.put("Irin", Map.of("hash", "9c8b6cf19894a5d97098bde9fa71808ba0c6c6ccad4610f181b1cfe827a6eccf", 
				"salt", "9cccf63bad04dbeb1764499dbd4ced9f"));// 1234
		users.put("Nikolas", Map.of("hash", "5ce6cfcef667e8e546bb04a19d67bcd5a70522a587551040450733850bf10fa4", 
				"salt", "86c1d795db43ecc7cfd999c8530498f4"));// 1234
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		HttpSession session = req.getSession();

		// 判斷使用者是否已登入
		Object loginStatus = session.getAttribute("loginStatus");
		if(loginStatus != null && Boolean.valueOf(loginStatus+"")) {
			// 放行
			chain.doFilter(request, response);
			
		} else {
			// 驗證 username 和 password
			String username = req.getParameter("username");
			String password = req.getParameter("password");
			
			// 如果沒有 username 的資料代表是第一次登入，重導至登入頁面
			if(username == null || username.trim().length() == 0) {
				resp.sendRedirect("/login");
				return;
			} 
			
			// 是否有此 user
			Map<String, String> user = users.get(username);
			if(user==null) {
				System.out.println("無此使用者");
				resp.sendRedirect("/login");
				return;
			} 
			
			// 判斷 password
			// 得到使用者的 hash 與 salt
			String hash = user.get("hash");
			String saltString = user.get("salt");
			byte[] salt = WebKeyUtil.hexStringToByteArray(saltString);
			
			// 透過密碼跟鹽巴算出來的 hash 要跟資料庫中的 hash 比對
			
			try {
				MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
				messageDigest.reset(); // 重置
				messageDigest.update(salt); // 加鹽
				byte[] inputHashedBytes = messageDigest.digest(password.getBytes());
				// 根據使用者輸入的 password 與已知的 salt 來產出 comparedHash
				String comparedHash = WebKeyUtil.bytesToHexString(inputHashedBytes);
				// 比較 comparedHash(運算的) 與 hash(已儲存的) 是否相等
				if(comparedHash.equals(hash)) {
					// 儲存登入狀態
					session.setAttribute("loginStatus", true);
					// 放行
					chain.doFilter(request, response);
					return;
				} else {
					System.out.println("登入失敗");
					resp.sendRedirect("/login");
					return;
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				resp.sendRedirect("/login");
			}
			
		}
		
	}

}
