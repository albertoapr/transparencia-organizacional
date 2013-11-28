package br.unirio.transparencia.model;

import java.io.Serializable;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class Usuario implements Serializable
{
	/**
	 * 
	 */
	//private static final long serialVersionUID = 1L;
	@Id
	private Long id;
	private TipoUsuario tipo;
	@Index
	private String nome;
	private String email;
	private String senha;
	private boolean ativo;
	

	public Usuario()
	{
	setAtivo(true);
	setTipo(TipoUsuario.USUARIO);
	}
	
	
	public Long getId()
	{
		return id;
	}
	
	public void setId(Long id)
	{
		this.id = id;
	}
	


	
	public String getName()
	{
		return nome;
	}
	
	public String getNome()
	{
		return nome;
	}
	
	public void setNome(String nome)
	{
		this.nome = nome;
	}

	
	public String getEmail()
	{
		return email;
	}
	
	public void setEmail(String email)
	{
		this.email = email;
	}
	


	

	
	public boolean isActive()
	{
		return ativo;
	}
	
	public boolean getAtivo()
	{
		return ativo;
	}
	
	public void setAtivo(boolean ativo)
	{
		this.ativo = ativo;
	}




	public TipoUsuario getTipo() {
		return tipo;
	}


	public void setTipo(TipoUsuario tipo) {
		this.tipo = tipo;
	}


	public String getSenha() {
		return senha;
	}


	public void setSenha(String senha) {
		this.senha = senha;
	}



}