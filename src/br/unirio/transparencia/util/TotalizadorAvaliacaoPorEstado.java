package br.unirio.transparencia.util;

import java.io.Serializable;

public class TotalizadorAvaliacaoPorEstado implements Comparable<TotalizadorAvaliacaoPorEstado>, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public TotalizadorAvaliacaoPorEstado(String estado, int total) {
		super();
		this.estado = ControladorEstados.getNome(estado);
		this.total = total;
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
	private String estado;
	private int total;
	@Override
	public int compareTo(TotalizadorAvaliacaoPorEstado outra) {
		 if (this.estado.compareTo(outra.estado)<0) {
		      return -1;
		    }

		    if (this.estado.compareTo(outra.estado)>0) {
		      return 1;
		    }

		    return 0;
		  }
	

}
