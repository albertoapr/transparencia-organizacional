package br.unirio.transparencia.config;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import br.unirio.transparencia.model.Mercadoria;
import br.unirio.transparencia.model.Usuario;


import com.googlecode.objectify.ObjectifyService;

/**
 * Componente necessário para registrar no Objectify quais são as entidades que ele deve gerenciar.
 * 
 * <p>Código executado durante a inicialização do aplicativo web.</p> 
 * 

 */
public class ConfigStartup implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		ObjectifyService.register(Mercadoria.class);
		ObjectifyService.register(Usuario.class);
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {}
	
}
