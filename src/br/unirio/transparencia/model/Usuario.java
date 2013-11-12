package br.unirio.transparencia.model;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class Usuario implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	private Long id;
	private TipoUsuario tipoUsuario;
	private String nome;
	private String email;
	private String senhaCodificada;
	private boolean ativo;
	private boolean forcaResetSenha;
	private boolean deveTrocarSenha;

	public Usuario()
	{
		this.id = (long) -1;
		this.tipoUsuario = TipoUsuario.VISITANTE;
		this.nome = "";
		this.email = "";
		this.senhaCodificada = "";
		this.ativo = true;
		this.setForcaResetSenha(false);
		this.deveTrocarSenha = true;
	}
	
	
	public Long getId()
	{
		return id;
	}
	
	public void setId(Long id)
	{
		this.id = id;
	}
	
	public TipoUsuario getTipoUsuario()
	{
		return tipoUsuario;
	}
	
	public void setTipoUsuario(TipoUsuario tipo)
	{
		this.tipoUsuario = tipo;
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
	
	public String getSenhaCodificada()
	{
		return this.senhaCodificada;
	}
	
	public void setSenhaCodificada(String senha)
	{
		this.senhaCodificada = senha;
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

	public boolean getForcaResetSenha()
	{
		return forcaResetSenha;
	}

	public void setForcaResetSenha(boolean forcaResetSenha)
	{
		this.forcaResetSenha = forcaResetSenha;
	}

	
	public boolean mustChangePassword()
	{
		return deveTrocarSenha;
	}
	
	public boolean getDeveTrocarSenha()
	{
		return deveTrocarSenha;
	}
	
	public void setDeveTrocarSenha(boolean flag)
	{
		this.deveTrocarSenha = flag;
	}


	public boolean checkLevel(String nivel)
	{
		return (tipoUsuario.getCodigo().compareToIgnoreCase(nivel) == 0);
	}
}