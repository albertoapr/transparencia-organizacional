transparencia-organizacional
===============

Aplicativo web, desenvolvido com o framework JavaServer Faces 2 (JSF) o framework web MVC padronizado pela especificação Java EE. Esse projeto foi desenvolvido com suporte a plataforma cloud (nuvem) do Google, o App Engine. A camada de persistência é implementada utilizando o Objectify, uma framework de persistência alto nível para o DataStore do App Engine.

O objetivo dessa aplicação é apoiar o processo de avaliação das organizações segundo o modelo de maturidade em transparência organizacional, utilizando uma tecnologia produtiva, e para implantação na nuvem.

Você pode acessar a aplicação no Google App Engine através da url: http://transparencia-organizacional.appspot.com

Detalhes da implementação
-------
Tecnologias utilizadas na implementação:
* JSF 2: utilizamos o framework JavaServer Faces, seguindo o modelo arquitetural MVC e o uso de componentes visuais para a construção das interfaces gráficas (front-end);
* Bootstrap: framework para front-end, define uma série de definições CSS e código JavaScript para a criação de layouts na web, incluindo tipografica, formulários, tabelas, etc.
* Objectify: framework que define uma API alto nível para lidar com o mecanismo de persistência do App Engine, o DataStore.
* App Engine: configurações necessárias para executar a aplicação no ambiente cloud do Google;

Pré-requisitos
-------
* JDK - versão 1.6 do Java, a versão suportada pelo App Engine;
* Eclipse - recomendamos o Eclipse justamente por contar com o plugin do Google para o App Engine, facilitando o trabalho de implantação.
* Plugin do Google para Eclipse - plugin com suporte a implantação no App Engine;
* Versão do SKD do App Engine: 1.7.3;
* JSF: utilizamos a implementação Mojarra versão 2.1, modificado para para ignorar InitialContext (hack da classe WebConfiguration);
* Objectify: versão 4;

Saiba mais
-------
Visite a página do projeto:
http://sites.google.com/site/transparenciaorganizacional/
