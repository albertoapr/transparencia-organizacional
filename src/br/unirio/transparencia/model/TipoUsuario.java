package br.unirio.transparencia.model;

public enum TipoUsuario
{
	VISITANTE("visit"),
	OPERADOR("op"),
	ADMINISTRADOR("adm");
	
	private String codigo;
	
	TipoUsuario(String codigo)
	{
		this.codigo = codigo;
	}
	
	public String getCodigo()
	{
		return codigo;
	}
	
	public static TipoUsuario get(String codigo)
	{
		for (TipoUsuario tipo : TipoUsuario.values())
			if (tipo.getCodigo().compareToIgnoreCase(codigo) == 0)
				return tipo;
		
		return null;
	}
}