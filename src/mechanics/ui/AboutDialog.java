package mechanics.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AboutDialog extends JDialog {

    public AboutDialog(JFrame parent) {
        super(parent, "Como Usar", false);
        setSize(600, 400);
        setLocationRelativeTo(parent);
        setModalityType(ModalityType.APPLICATION_MODAL);

        setLayout(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        JTextPane text = new JTextPane();
        text.setContentType("text/html");
        text.setText(getText());
        text.setEditable(false);
        text.setCaret(null);
        text.setBorder(new EmptyBorder(5, 5, 5, 5));
        scrollPane.setViewportView(text);

        add(scrollPane, BorderLayout.CENTER);
    }

    public String getText() {
        return """
                <html style="text-align:justify">
                <h3>Bem-vindo(a) ao Oscilador Bidimensional! :)</h3>
                <p>
                Este programa simula um problema físico em que uma massa (que pode ter quantos quilogramas você desejar),
                está presa entre quatro molas (que podem ter características diferentes), e o que acontece com as oscilações
                conforme o tempo passa.</p>
                <br>
                <p>
                Você pode configurar onde você quer que a massa comece (x(0) e y(0), onde "x" é
                a direção horizontal e y a vertical), e qual a velocidade inicial dela (vx(0) e vy(0)). Note que todos esses
                quatro valores podem ser negativos! O "0" nesse caso representa o centro da simulação.
                </p>
                <br>
                <p>
                Alguns fenômenos interessantes podem acontecer quando você muda o Kx ou Ky! São chamadas de "Figuras de Lissajous",
                e uma das maneiras de fazê-las aparecer é colocando o Kx e o Ky como números quadrados perfeitos. Por exemplo,
                teste colocando o Kx = 9, e o Ky = 4. Depois, tente Kx = 121, e Ky = 81. Uma coisa importante é que, nestes casos,
                a massa está sempre passando pelo mesmo caminho, por mais complicado ou simples que seja. Enquanto se você colocar
                Kx e Ky com valores que não são quadrados perfeitos, provavelmente o objeto vai percorrer um caminho que é sempre diferente.
                (Isso ocorre pois a frequência de oscilação em cada direção é a raiz da razão k/m, então se a frequência de uma direção
                for um número irracional vezes a frequência da outra, o sistema nunca voltará ao seu estado inicial, ou qualquer outro.)
                </p>
                <h3>Controles da simulação:</h3>
                <p>
                Você também pode controlar o tempo! "Resetar" reinicia a simulação na configuração inicial. "Pausar" para o tempo.
                A "Velocidade" é um fator que controla o quão rápido o tempo passa: um fator "1" indica que o tempo passa como na vida real.
                Um fator "2" indica que ele passa duas vezes mais rápido que a vida real, um fator "0,1" indica 10 vezes mais lento, e por aí vai.
                </p>
                <br>
                <p>
                Além disso, há opções (na esquerda) para controlar a visualização. Você pode escolher quaisquer vetores disponíveis
                entre a posição, a velocidade, e a força sobre a massa. Vetores que possuem um "x" ou "y" no final indicam que são vetores
                <i>apenas</i> naquela direção, enquanto os que não têm nada (r, v, e F) são os vetores com as duas componentes. Se a bola for
                grande demais e estiver atrapalhando na visualização, você pode diminuir seu tamanho no "diâmetro da bola".
                </p>
                <p>
                Por fim, a "Resolução" controla quantas vezes por segundo a animação vai atualizar. Se você perceber que ela
                parece meio travada às vezes, tente aumentar para 120. (Tenha em mente que você não vai enxergar nada mais "suave" que a taxa de atualização
                da sua tela, em geral 60Hz ou 120/144Hz). Mas além disso, a resolução pode ser útil para tornar o caminho percorrido mais preciso, principalmente nos casos
                em que a massa está se movendo muito rapidamente. Note, porém, que provavelmente vão aparecer limitações na capacidade de processamento
                do seu computador.
                </p>
                <h3>O Arrasto Linear</h3>
                <p>
                Para fechar o tutorial, tem uma configuração que ainda não expliquei: o arrasto linear (cujo valor é dado por "b"). Imagine ele como uma espécie de
                "atrito" entre a massa e algum meio que ela esteja se movendo. Pode ser o vácuo (aí o arrasto é 0), o ar, a água,
                ou até algo mais viscoso como mel. Aumentando o valor do arrasto vai fazer com que, eventualmente, a sua bolinha perca
                a energia e volte para o centro, que é o que aconteceria na vida real. É interessante que, se você colocar um arrasto muito
                pequeno (pequeno sempre em comparação com a massa que você colocou!), o objeto continua a oscilar, mas chegando cada vez
                mais próximo ao centro. Enquanto se você coloca um arrasto enorme, ele sequer oscila, mas só vai indo lentamente o centro.
                </p>
                <p><b>Curiosidade: quanto o maior o arrasto, não necessariamente mais rápido ele vai ao centro! Pelo contrário, a partir
                de um certo ponto, quanto maior o valor de "b", mais ele demora para voltar.</b></p>
                </html>
                """;
    }
}
