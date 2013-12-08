package br.unirio.transparencia.util;

import java.io.Serializable;

public class TotalizadorAvaliacoes implements Comparable<TotalizadorAvaliacoes>, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String estado;
	private int total;
	private int totalOpaca;
	private int totalCompreendida;
	private int totalDivulgada;
	private int totalConfiavel;
	private int totalParticipativa;
	
	
	public TotalizadorAvaliacoes(String estado)
	{
		super();
		if (estado.compareTo("Todos")!=0)
			this.estado = ControladorEstados.getNome(estado);
		else
			this.estado=estado;
		this.total = 0;
		this.totalOpaca=0;
		this.totalCompreendida=0;
		this.totalConfiavel=0;
		this.totalDivulgada=0;
		this.totalParticipativa=0;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	
	@Override
	public int compareTo(TotalizadorAvaliacoes outra) {
		 if (this.estado.compareTo(outra.estado)<0) {
		      return -1;
		    }

		    if (this.estado.compareTo(outra.estado)>0) {
		      return 1;
		    }

		    return 0;
		  }
	public int getTotalOpaca() {
		return totalOpaca;
	}
	public void setTotalOpaca(int totalOpaca) {
		this.totalOpaca = totalOpaca;
	}
	public int getTotalCompreendida() {
		return totalCompreendida;
	}
	public void setTotalCompreendida(int totalCompreendida) {
		this.totalCompreendida = totalCompreendida;
	}
	public int getTotalDivulgada() {
		return totalDivulgada;
	}
	public void setTotalDivulgada(int totalDivulgada) {
		this.totalDivulgada = totalDivulgada;
	}
	public int getTotalConfiavel() {
		return totalConfiavel;
	}
	public void setTotalConfiavel(int totalConfiavel) {
		this.totalConfiavel = totalConfiavel;
	}
	public int getTotalParticipativa() {
		return totalParticipativa;
	}
	public void setTotalParticipativa(int totalParticipativa) {
		this.totalParticipativa = totalParticipativa;
	}
	

}
