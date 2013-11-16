package br.unirio.transparencia.dao;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.Serializable;
import java.util.List;

import br.unirio.transparencia.model.Usuario;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFilter;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.Query;



/**
 * Implementa o contrato de persistência da entidade <code>Usuario</code>.
 * 
 * <p>
 *   Nessa aplicação resolvemos a persistência utilizando o Objectify, um framework de persistência para o App Engine.<br/>
 *   A proposta do Objetify é denifir uma API mais alto-nível para manipular dados no <code>DataStore</code> do App Engine.
 * </p>
 * 
 * @see br.com.yaw.sjpac.dao.UsuarioDAO
 * @see com.googlecode.objectify.ObjectifyService.
 * 
 * @author YaW Tecnologia
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
	
	@Override
	public List<Usuario> getAll() {
		return ofy().load().type(Usuario.class).list();
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
		
		List<Usuario> usuarios =ofy().load().type(Usuario.class).list();
		for (Usuario user:usuarios)
		{
			if (user.getEmail().compareTo(email)==0)
				return user;
		}	
		return null;	
		
		
	}
}
