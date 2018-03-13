package com.bots.bots.resources;

public enum Constantes {
	USUARIOS;
	
	public String getString() {
		switch(this) {
			case USUARIOS: return "usuarioses";
		}
		return "/";
	}
}
