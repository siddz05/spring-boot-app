/**
 * 
 */
package com.sid.demo.sidproject;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author sid to generate commands -- > alt + shift + j
 *
 */
@RestController
@RequestMapping(value = "/request")
public class RequestMappingController {

	/**
	 * test simple GET
	 * 
	 * @return
	 */
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	@ResponseBody
	String testGet() {
		String result = "";
		result = "sid GET";
		return result;
	}

	/**
	 * test simple POST
	 * 
	 * @return
	 */
	@RequestMapping(value = "/post", method = RequestMethod.POST)
	@ResponseBody
	String testPost() {
		String result = "";
		result = "sid POST";
		return result;
	}

	/**
	 * test simple get with header using RequestHeaderS
	 * 
	 * @param name header param
	 * @return
	 */
	@RequestMapping(value = "/getHead", method = RequestMethod.GET)
	@ResponseBody
	String testGetHead(@RequestHeader("name") String name) {
		String result = "";
		result = "sid GET HEAD " + name;
		return result;
	}

	/**
	 * It will only accept a header with value application/json or text/plain or any other valid header. as
	 * content-type
	 * @return A Json
	 */
	@RequestMapping(value = "/getJson", method = RequestMethod.GET, consumes = {
			"application/json","text/plain","*" }, produces = { "application/json" })
	@ResponseBody
	Map<String, String> testGetWithproducesAndConsumeJson() {
		String result = "";
		Map<String,String> hashMap = new HashMap<>();
		result = "sid Consumes only application json";
		hashMap.put("key", result);
		return hashMap;
	}
	
	
	/**
	 * @param name -- a path varibale with optional param
	 * @return a simple String
	 */
	@RequestMapping(value = {"/getPathVariable/{name}","/getPathVariable"}, method = RequestMethod.GET)
	@ResponseBody
	String testPathVariable(@PathVariable(value="name") Optional<String> name) {
		String result = "";
		result = "Testing Path Valriable With Optional Param ";
		result+= name.isPresent()?name.get().toString():"Default Name";
		return result;
	}
}
