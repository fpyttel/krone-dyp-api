package de.fpyttel.kronedyp.api.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.fpyttel.kronedyp.api.dao.PlayerBF;

@RestController
@RequestMapping("/player")
public class PlayerController {

	@Autowired
	private PlayerBF playerBF;

	private Gson gson = new GsonBuilder().serializeNulls().create();

	@RequestMapping(value = "/{playerId}", produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	String getPlayer(@PathVariable("playerId") Integer playerId, HttpServletResponse response,
			HttpServletRequest request) {
		return gson.toJson(playerBF.getPlayer(playerId));
	}

	@RequestMapping(value = "/{playerId}/positions", produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	String getPositions(@PathVariable("playerId") Integer playerId, HttpServletResponse response,
			HttpServletRequest request) {
		return gson.toJson(playerBF.getPositions(playerId, request.getLocale()));
	}

	@RequestMapping(value = "/{playerId}/eloHistory", produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	String getEloHistory(@PathVariable("playerId") Integer playerId, HttpServletResponse response,
			HttpServletRequest request) {
		return gson.toJson(playerBF.getEloHistory(playerId));
	}
	
	@RequestMapping(value = "/{playerId}/positionsHistory", produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	String getPositionHistory(@PathVariable("playerId") Integer playerId, HttpServletResponse response,
			HttpServletRequest request) {
		return gson.toJson(playerBF.getPositionsHistory(playerId));
	}
	
	@RequestMapping(value = "/{playerId}/teammates", produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	String getTeammates(@PathVariable("playerId") Integer playerId, HttpServletResponse response,
			HttpServletRequest request) {
		return gson.toJson(playerBF.getTeammates(playerId));
	}
}
