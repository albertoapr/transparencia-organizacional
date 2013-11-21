/**
 * 
 */
package br.unirio.transparencia.dao;

import java.util.List;

import br.unirio.transparencia.model.Profissional;

/**
 * @author alberto
 *
 */
public interface ProfissionalDAO {
	/**
	 * Faz a inserção ou atualização da profissional na base de dados.
	 * @param profissional
	 * @return o id objeto persistido.
	 * @throws <code>RuntimeException</code> se algum problema ocorrer.
	 */
	Long save(Profissional profissional);
	
	/**
	 * @return Lista com todas as profissionals cadastradas no banco de dados.
	 * @throws <code>RuntimeException</code> se algum problema ocorrer.
	 */
	List<Profissional> getAll();
	
	/**
	 * Exclui o registro da profissional na base de dados 
	 * @param profissional
	 * @return true
	 * @throws <code>RuntimeException</code> se algum problema ocorrer.
	 */
	Boolean remove(Profissional profissional);
	
	/**
	 * @param id filtro da pesquisa.
	 * @return Profissional com filtro no id, caso nao exista retorna <code>null</code>.
	 * @throws <code>RuntimeException</code> se algum problema ocorrer.
	 */
	Profissional findById(Long id);

	/**
	 * @param id filtro da pesquisa.
	 * @return Profissional com filtro no id, caso nao exista retorna <code>null</code>.
	 * @throws <code>RuntimeException</code> se algum problema ocorrer.
	 */
	Profissional findByEmail(String email);
}



