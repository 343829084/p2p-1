package com.esoft.archer.user.valide;

import java.util.Map;

public interface LeagueListValidator {

	 public void getleagueList(Map<String, String> map) throws RuntimeException;
	 public void getleagueByid(Map<String, String> map) throws RuntimeException;
	 public void save(Map<String, String> map) throws RuntimeException;
}
