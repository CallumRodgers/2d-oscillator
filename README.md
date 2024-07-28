# Oscilador Bidimensional

Esta é a página do oscilador no GitHub, que contém os códigos e os executáveis
do programa. Sinta-se à vontade para reportar erros, explorar o código, ou até criar
Pull Requests caso queira ampliar o software.

Sobre o projeto: o programa simula um corpo preso a quatro molas (uma de cada lado), e qual o movimento
que ela descreve ao longo do tempo, podendo variar a massa do corpo, sua posição e velocidade iniciais, o
quão "fortes" são as molas, e até mesmo uma situação com arrasto linear (como um corpo sendo freado por atravessar
um fluido). Além disso, você pode visualizar exatamente a força total agindo sobre o corpo a cada instante, 
assim como sua velocidade e posição.

# Instalação

## Instale a versão 22+ do Java:

### Windows & MacOS:

A versão mais recente pode ser encontrada em https://www.java.com/pt/download/

### Linux:

Ubuntu (e derivados): 

$ `sudo apt install default-jre`

Arch (e derivados):

$ `sudo pacman -S --needed jre-openjdk`

## Baixe o arquivo .jar

Faça o download do arquivo executável, disponível aqui no GitHub, na aba [Releases](https://github.com/CallumRodgers/2d-oscillator/releases/tag/v1.0.0)

## Execute o jar

Para executar o arquivo (uma vez que a versão apropriada do Java esteja instalada), você pode escolher
entre clicar no arquivo diretamente para rodá-lo, ou executá-lo em um terminal. Se a primeira maneira não funcionar, abra
o Prompt de Comando (Windows) ou seu terminal de preferência, e navegue até a pasta onde você fez o download do arquivo
(provavelmente Downloads ou Área de Trabalho), e execute:

`java -jar Oscillator.jar` (Serve para qualquer sistema com Java instalado)

Se nada disso funcionar, sinta-se à vontade para pedir ajuda em callumrodgers@id.uff.br :)

# Compilando localmente

Caso seja de seu interesse modificar/ampliar o oscilador e testá-lo, você pode usar um editor de Java de sua preferência
(recomendo IntelliJ), e abrir um novo projeto diretamente do repositório do GitHub. A URL é: 
https://github.com/CallumRodgers/2d-oscillator.git

Daí, você precisará do JDK (Java Development Kit), e não apenas o Java comum. Provavelmente o IntelliJ vai te alertar de
qualquer problema e oferecer uma solução. No fundo, basta ter o JDK 22 (ou mais recente) para executar. E, imagino que,
se é de seu interesse alterar o código, você já deva ter uma ideia de como preparar o ambiente ;)

Como sempre, qualquer dúvida meu email está disponível.
