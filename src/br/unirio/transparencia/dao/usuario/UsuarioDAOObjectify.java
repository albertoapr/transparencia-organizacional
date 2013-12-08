package br.unirio.transparencia.dao.usuario;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.Serializable;
import java.util.List;

import br.unirio.transparencia.model.TipoUsuario;
import br.unirio.transparencia.model.Usuario;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;



/**
 * Implementa o contrato de persistência da entidade <code>Usuario</code>.
 * 
 * <p>
 *   Nessa aplicação resolvemos a persistência utilizando o Objectify, um framework de persistência para o App Engine.<br/>
 *   A proposta do Objetify é denifir uma API mais alto-nível para manipular dados no <code>DataStore</code> do App Engine.
 * </p>
 * 

 * @see com.googlecode.objectify.ObjectifyService.
 * 

 */
public class UsuarioDAOObjectify implements Serializable, UsuarioDAO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Long save(Usuario usuario) {
		ofy().save().entity(usuario).now();
		return usuario.getId();
	}
	
	private Usuario criaAdministradorPadrao(){
		/* Cria um Usuário Administrador Padrão */
		Usuario administrador = new Usuario();
		administrador.setAtivo(true);
		administrador.setEmail("transparencia.contato@gmail.com");
		administrador.setNome("Transparencia");
		administrador.setId((long) 0);
		administrador.setTipo(TipoUsuario.ADMINISTRADOR);
		administrador.setSenha("123456");
		
		
		return administrador;
	}
	
	@Override
	public List<Usuario> getAll() {
		Boolean existeAdministrador =false;
		
		List<Usuario> usuarios =ofy().load().type(Usuario.class).list();
		for(Usuario usuario:usuarios){
			if (usuario.getTipo()==TipoUsuario.ADMINISTRADOR)
			{
				existeAdministrador =true;
				break;
			}
		}
		/*cria um usuário administrador padrão para operar o sistema, se ainda não houver*/
	   if (!existeAdministrador){
		  Usuario administrador = criaAdministradorPadrao();
		  usuarios.add(administrador);
		}
		return usuarios;
	}
	
	@Override
	public Boolean remove(Usuario usuario) {
		ofy().delete().entity(usuario).now();
		return true;
	}
	
	@Override
	public Usuario findById(Long id) {
		Key<Usuario> k = Key.create(Usuario.class, id);
		return ofy().load().key(k).get();
	}

	@Override
	public Usuario findByEmail(String email) {
		List<Usuario> usuarios = ofy().load().type(Usuario.class).list();
		if (usuarios.size()>0)
		{
			Ref<Usuario> usuario =ofy().load().type(Usuario.class).filter("email", email).first();
			if (usuario!=null)
			{
				return usuario.get();
			}
		}
		else{
			return	criaAdministradorPadrao();
		}
		
		return null;
		
	}
}
