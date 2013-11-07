package br.unirio.transparencia.dao;

import java.util.List;

import br.unirio.transparencia.model.Mercadoria;

/**
 * Contrato de persistência para a entidade <code>Mercadoria</code>. 
 * 
 * <p>Define as operações basicas de cadastro (CRUD), seguindo o design pattern <code>Data Access Object</code>.</p>
 * 
 * @author YaW Tecnologia
 */
public interface MercadoriaDAO {

	/**
	 * Faz a inserção ou atualização da mercadoria na base de dados.
	 * @param mercadoria
	 * @return o id objeto persistido.
	 * @throws <code>RuntimeException</code> se algum problema ocorrer.
	 */
	Long save(Mercadoria mercadoria);
	
	/**
	 * @return Lista com todas as mercadorias cadastradas no banco de dados.
	 * @throws <code>RuntimeException</code> se algum problema ocorrer.
	 */
	List<Mercadoria> getAll();
	
	/**
	 * Exclui o registro da mercadoria na base de dados 
	 * @param mercadoria
	 * @return true
	 * @throws <code>RuntimeException</code> se algum problema ocorrer.
	 */
	Boolean remove(Mercadoria mercadoria);
	
	/**
	 * @param id filtro da pesquisa.
	 * @return Mercadoria com filtro no id, caso nao exista retorna <code>null</code>.
	 * @throws <code>RuntimeException</code> se algum problema ocorrer.
	 */
	Mercadoria findById(Long id);
}
