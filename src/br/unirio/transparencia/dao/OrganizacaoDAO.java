package br.unirio.transparencia.dao;

import java.util.List;

import br.unirio.transparencia.model.Organizacao;

/**
 * Contrato de persistência para a entidade <code>Organizacao</code>. 
 * 
 * <p>Define as operações basicas de cadastro (CRUD), seguindo o design pattern <code>Data Access Object</code>.</p>
 * 
 * @author YaW Tecnologia
 */
public interface OrganizacaoDAO {

	/**
	 * Faz a inserção ou atualização da organizacao na base de dados.
	 * @param organizacao
	 * @return o id objeto persistido.
	 * @throws <code>RuntimeException</code> se algum problema ocorrer.
	 */
	Long save(Organizacao organizacao);
	
	/**
	 * @return Lista com todas as organizacaos cadastradas no banco de dados.
	 * @throws <code>RuntimeException</code> se algum problema ocorrer.
	 */
	List<Organizacao> getAll();
	
	/**
	 * Exclui o registro da organizacao na base de dados 
	 * @param organizacao
	 * @return true
	 * @throws <code>RuntimeException</code> se algum problema ocorrer.
	 */
	Boolean remove(Organizacao organizacao);
	
	/**
	 * @param id filtro da pesquisa.
	 * @return Organizacao com filtro no id, caso nao exista retorna <code>null</code>.
	 * @throws <code>RuntimeException</code> se algum problema ocorrer.
	 */
	Organizacao findById(Long id);
}
