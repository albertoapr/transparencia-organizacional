package br.unirio.transparencia.util;

import java.io.Serializable;

import br.unirio.transparencia.model.NivelTransparencia;

public class TotalizadorAvaliacaoPorNivel implements Serializable{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private NivelTransparencia nivel;
private int total;

public TotalizadorAvaliacaoPorNivel(NivelTransparencia nivel, int total) {
	super();
	this.nivel = nivel;
	this.total = total;
}
public NivelTransparencia getNivel() {
	return nivel;
}
public void setNivel(NivelTransparencia nivel) {
	this.nivel = nivel;
}
public int getTotal() {
	return total;
}
public void setTotal(int total) {
	this.total = total;
}
}
