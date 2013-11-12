package br.unirio.transparencia.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import br.unirio.transparencia.model.Usuario;

/**
 * Contrato de persistência para a entidade <code>Usuario</code>. 
 * 
 * <p>Define as operações basicas de cadastro (CRUD), seguindo o design pattern <code>Data Access Object</code>.</p>
 * 
 * 
 */
public interface UsuarioDAO {

	/**
	 * Faz a inserção ou atualização da usuario na base de dados.
	 * @param usuario
	 * @return o id objeto persistido.
	 * @throws <code>RuntimeException</code> se algum problema ocorrer.
	 */
	Long save(Usuario usuario);
	
	/**
	 * @return Lista com todas as usuarios cadastradas no banco de dados.
	 * @throws <code>RuntimeException</code> se algum problema ocorrer.
	 */
	List<Usuario> getAll();
	
	/**
	 * Exclui o registro da usuario na base de dados 
	 * @param usuario
	 * @return true
	 * @throws <code>RuntimeException</code> se algum problema ocorrer.
	 */
	Boolean remove(Usuario usuario);
	
	/**
	 * @param id filtro da pesquisa.
	 * @return Usuario com filtro no id, caso nao exista retorna <code>null</code>.
	 * @throws <code>RuntimeException</code> se algum problema ocorrer.
	 */
	Usuario findById(Long id);

	UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException, DataAccessException;
}
