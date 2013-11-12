package br.unirio.transparencia.dao;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.Serializable;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import br.unirio.transparencia.model.Usuario;

import com.googlecode.objectify.Key;

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
public class UsuarioDAOObjectify implements Serializable, UsuarioDAO , UserDetailsService{

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
	
	
	@SuppressWarnings("deprecation")
	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException, DataAccessException
			{
		System.out.println("Getting access details from employee dao !!");

		// Ideally it should be fetched from database and populated instance of
		// #org.springframework.security.core.userdetails.User should be returned from this method
		UserDetails user = new User(username, "password", true, true, true, true,
				new GrantedAuthority[]{ new GrantedAuthorityImpl("ROLE_USER") });
		return user;
			}
	
}
