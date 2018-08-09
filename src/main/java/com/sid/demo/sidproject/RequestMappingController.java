/**
 * 
 */
package com.sid.demo.sidproject;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
	 * It will only accept a header with value application/json or text/plain or any
	 * other valid header. as content-type
	 * 
	 * @return A Json
	 */
	@RequestMapping(value = "/getJson", method = RequestMethod.GET, consumes = { "application/json", "text/plain",
			"*" }, produces = { "application/json" })
	@ResponseBody
	Map<String, String> testGetWithproducesAndConsumeJson() {
		String result = "";
		Map<String, String> hashMap = new HashMap<>();
		result = "sid Consumes only application json";
		hashMap.put("key", result);
		return hashMap;
	}

	/**
	 * @param name -- a path varibale with optional param
	 * @return a simple String
	 */
	@RequestMapping(value = { "/getPathVariable/{name}", "/getPathVariable" }, method = RequestMethod.GET)
	@ResponseBody
	String testPathVariable(@PathVariable(value = "name") Optional<String> name) {
		String result = "";
		result = "Testing Path Valriable With Optional Param ";
		result += name.isPresent() ? name.get().toString() : "Default Name";
		return result;
	}

	/**
	 * @param name -- string
	 * @param age  -- long
	 * @return A stirng which takes both the path variable in action
	 */
	@RequestMapping(value = { "/getPathVariable/{name}/age/{age}" }, method = RequestMethod.GET)
	@ResponseBody
	String testMultiplePathVariable(@PathVariable(value = "name") Optional<String> name, @PathVariable Long age) {
		String result = "";
		result = "Testing Path Valriable With Optional Param ";
		result += name.isPresent() ? name.get().toString() : "Default Name";
		result += " Age Is " + age;
		return result;
	}

	/**
	 * @param numericId A regex driven pathVariable
	 * @return
	 */
	@RequestMapping(value = { "/getPathVariableN/{numericId:[\\d]+}" }, method = RequestMethod.GET)
	@ResponseBody
	String testPathVariableWithRegex(@PathVariable Long numericId) {
		String result = "";
		result = "Testing Path Valriable With RegeX " + numericId;
		return result;
	}

	/**
	 * @param name -- required param
	 * @return String
	 */
	@RequestMapping(value = { "/getRequestParam" }, method = RequestMethod.GET)
	@ResponseBody
	String getRequestParam(@RequestParam("name") String name) {
		String result = "";
		result = "Testing RequestParam " + name;
		return result;
	}

	/**
	 * @param name -- optinal Param, with a default Value
	 * @return String
	 */
	@RequestMapping(value = { "/getOptionalRequestParam" }, method = RequestMethod.GET)
	@ResponseBody
	String getOptionalRequestParam(
			@RequestParam(name = "name", required = false, defaultValue = "DefaultName") String name) {
		String result = "";
		result = "Testing Optinal Default RequestParam " + name;
		return result;
	}

	/**
	 * @param request  -- generic HttpRequest
	 * @param response -- Generic ServlateResponse
	 * @return it will return acc. the ReuquestMethod Type -- GET POST PUT or DELETE
	 */
	@RequestMapping(value = { "/getMultipleRequestMaping" }, method = { RequestMethod.GET, RequestMethod.POST,
			RequestMethod.PUT, RequestMethod.PATCH, RequestMethod.DELETE })
	@ResponseBody
	String getMultipleRequestMaping(HttpServletRequest request, HttpServletResponse response) {
		String result = "";

		if (request.getMethod().equals("GET")) {
			result = "Testing getMultipleRequestMaping GET";
			return result;
		} else if (request.getMethod().equals("POST")) {
			result = "Testing getMultipleRequestMaping POST";
			return result;
		} else if (request.getMethod().equals("PUT")) {
			result = "Testing getMultipleRequestMaping PUT";
			return result;
		} else {
			result = "Testing getMultipleRequestMaping DELETE";
			return result;
		}
	}

	/**
	 * @return Simple FallbaCK iMPL FOR aLL gET rEQUEST
	 */
	@RequestMapping(value = { "/*" }, method = RequestMethod.GET)
	@ResponseBody
	String fallForGetRequest() {
		String result = "";
		result = "All Get RequestHitting me!! /request";
		return result;
	}

	/**
	 * Below this Line, Shortcut MEhtods for GET, POST, PUT and DELETE Above Spring
	 * 4.3
	 *
	 */

	@GetMapping("/getMapping")
	@ResponseBody
	String getMappingShortcut() {
		String result = "";
		result = "All Get getMappingShortcut!!";
		return result;
	}

	@PostMapping("/postMapping")
	@ResponseBody
	String postMappingShortcut() {
		String result = "";
		result = "All Get postMappingShortcut!!";
		return result;
	}

	@PutMapping("/putMapping")
	@ResponseBody
	String putMappingShortcut() {
		String result = "";
		result = "All Get putMappingShortcut!!";
		return result;
	}

	@DeleteMapping("/deleteMapping")
	@ResponseBody
	String deletMappingShortcut() {
		String result = "";
		result = "All Get deletMappingShortcut!!";
		return result;
	}

}
