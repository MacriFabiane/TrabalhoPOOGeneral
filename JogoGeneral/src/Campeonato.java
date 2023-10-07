import java.util.Scanner;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;

public class Campeonato implements Serializable {
    private Jogador[] players = new Jogador[10]; // vetor dos jogadores do campeonato
    private int contJogadores = 0;
    private Scanner teclado = new Scanner(System.in);// scanf do java
    private int[] vet= new int[13];
    private String nome, biotipo;

    public Campeonato() {
        for (int i = 0; i < players.length; i++) {
            players[i] = null; // Define cada elemento como nulo
        }
    }

    public void incluirjogador() {
        if (contJogadores < 10 && players[contJogadores] == null) {

            System.out.println("Nome do Jogador(a): ");
            nome = teclado.nextLine();

            do{
                System.out.println("Tipo do Jogador [H-Humano ou M-Máquina]: ");
                biotipo = teclado.nextLine();
    
            }while(!biotipo.equals("H") && !biotipo.equals("h") && !biotipo.equals("M") && !biotipo.equals("m"));//tratamento de dados pra caso o biotipo for diferente de humano ou máquina

            Jogador jogador = new Jogador(nome, biotipo);
            players[contJogadores] = jogador;
            contJogadores++;

        } else {
            System.out.println("Não podem ser incluídos novos jogadores(as)");
        }

    } 

    public void removerJogador() {
        System.out.println("Jogadores:");
        for (int i = 0; i < contJogadores; i++) { // printar o nome dos jogadores
            System.out.println(i+" - "+ players[i].getNome());
        }
        System.out.println("Digite o nome do jogador:");
        nome = teclado.nextLine();
        int i=0;
        boolean verifica=false;
        do{
            if (nome.equals(players[i].getNome())) {
                players[i].dell();
                for (int j = i; j < contJogadores; j++) {
                    players[j] = players[j+1];// vai "puxando" os que vem depois pro lugar do exclindo e reordenando
                }
                contJogadores--;// diminui a quantidade total de jogadores para que, se o usuario quiser, possa adicionar outro
                verifica=true;//verifica que teve um jogador
                break;
            }
            i++;
        }while(i!=contJogadores);
        
        
        if (verifica==false){//caso não haja o jogador
                System.out.println("Jogador(a) inexistente");
        }
        else{
            System.out.println("Jogador(a) excluido com sucesso");
        }

    }

    public void iniciarCampeonato() {//inicia ou reinicia um campeonato
        for (int j = 0; j < 13; j++) {
            for (int i = 0; i < contJogadores; i++) {
                System.out.println(">>Rolando dados para " + players[i].getNome());
                System.out.print("Valores obtidos: ");// imprime sem pular a linha pros dados ficarem do lado
                players[i].getJogo().rolarDados();
                players[i].getJogo().mostrarDados();
                int opcao = 0;

 
                if (players[i].getTipoJogador().equals("H")|| players[i].getTipoJogador().equals("h")) {
                    opcao=0;
                    do {
                        System.out.println("Para qual jogada deseja marcar: [1-13] " + players[i].getNome() + "?");
                        System.out.println("1\t2\t3\t4\t5\t6\t7(T)\t8(Q)\t9(F)\t10(S+)\t11(S-)\t12(G)\t13(X)");
                        players[i].mostrarJogadasExecutadas();

                        while(opcao<=0|| opcao>13 || players[i].getJogo().getJogadas(opcao-1)!=-1){//caso o usuário tente escolher uma opcao inexistente ou alguma jogada já feita de novo
                            opcao = teclado.nextInt();
                            teclado.nextLine(); // Limpar o buffer de entrada após a leitura do inteiro
                            if(opcao<=0||opcao>13 || players[i].getJogo().getJogadas(opcao-1)!=-1){
                                System.out.println("Jogada inválida, escolha outra.");
                            }
                        }
                        if (players[i].getJogo().getJogadas(opcao-1)==-1) { //se a jogada ainda nao tiver sido feita

                            this.players[i].getJogo().setJogadas(opcao-1, this.players[i].getJogo().pontuarJogada(opcao));
                           
                        } 
                    } while (players[i].getJogo().getJogadas(opcao-1)==-1);

                }
                else if (players[i].getTipoJogador().equals("M")|| players[i].getTipoJogador().equals("m")){   // Se for do tipo máquina irá escolher a melhor jogada
                    opcao=0;
                    int melhorPontuacao = 0;
                    int melhorJogada=0;
                        while(opcao<13){//basicamente vai ver para aquela rodada qual vai ser a jogada com maior pontuação
                        if(this.players[i].getJogo().getJogadas(opcao)==-1){//se já não for ocupada a jogada
                            int pontuacao = this.players[i].getJogo().pontuarJogada(opcao+1);
                            
                            if(pontuacao>=melhorPontuacao){//serve p achar a melhor jogada mas vai acabar preenchendo todas as outras do vetor jogadas tbm
                                melhorPontuacao=pontuacao;
                                melhorJogada=opcao;
                                vet[melhorJogada] = 1;//se a jogada já tiver sido usada anteriormente é marcada como 1;
                            }
                            
                        }
                        // else{
                        //     vet[melhorJogada] = 1;//se a jogada já tiver sido usada anteriormente é marcada como 1;
                        // }
                        
                        opcao++;
                    }
                    
                    for (int k=0; k<13; k++){
                        if(this.players[i].getJogo().getJogadas(k)!= melhorPontuacao && vet[k]!=1){
                            this.players[i].getJogo().setJogadas(k, -1); //resolve o problema de preenchimento de outras jogadas
                        }
                    }  
                    this.players[i].getJogo().setJogadas(melhorJogada, melhorPontuacao);//pontua para a máquina             
                    System.out.println("1\t2\t3\t4\t5\t6\t7(T)\t8(Q)\t9(F)\t10(S+)\t11(S-)\t12(G)\t13(X)");
                    players[i].mostrarJogadasExecutadas();
                    System.out.println("Jogada que a maquina escolheu: "+ (melhorJogada+1));//retorna a jogada feita pela maquina melhorjogada(posição do vet)+1(pra ficar o "nome" da jogada certinho)
                }
            }
        }
    }

    public void mostrarCartela() {
        System.out.println("-- Cartela de Resultados --");
        System.out.print("Jogada\t");

        for(int i=0; i<contJogadores; i++){
            System.out.print(players[i].getNome()+"("+players[i].getTipoJogador()+")\t");
        } // vai imprimir o nome e o tipo de todos os players lado a lado
        System.out.print("\n"); //pula linha quando os nomes terminam

        String[] type={"1", "2", "3", "4", "5", "6", "7(T)", "8(Q)", "9(F)", "10(S+)", "11(S-)", "12(G)", "13(X)"};//string com os "nomes" das jogadas
        
       for(int j=0; j<13;j++){
        System.out.print(type[j]+"\t");//imprime os nomes das jogadas
        
        for(int k=0; k<contJogadores; k++){
            System.out.print(players[k].getJogoGeneral(j)+"\t\t"); // pega as pontuações jogadas de uma "ficha" dos jogadores que é o jogogeneral
        }
        System.out.print("\n");
       }

       System.out.println("--------------------------------------");
       System.out.print("Total\t");

       for(int k=0; k<contJogadores; k++){
            System.out.print(somaJogadas(k)+"\t\t");
        }
        System.out.print("\n");

    }
    public int somaJogadas(int jogante){
            int soma=0;
            for(int j=0; j<13; j++){//pra percorrer todos od jogos de cada jogador
                soma+=players[jogante].getJogoGeneral(j);
            }  
            return soma; 
    }

    public void gravarEmArquivo() {
        /* Gravar em arquivo */
        File arquivo = new File("Campeonato.dat");
        try {
            FileOutputStream fout = new FileOutputStream(arquivo);
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            // gravando o vetor de pessoas no arquivo
            oos.writeObject(players); //gravando o dado dos players, que é o único tipo que dá pra gravar e ler por se array(seguindo os exemplos da professora lá da classe pessoa)
            oos.flush();
            oos.close();
            fout.close();

        } catch (Exception ex) {
            System.err.println("erro: " + ex.toString());
        }

    }

    public void lerDoArquivo() {
        File arquivo = new File("Campeonato.dat");

        try {
            FileInputStream fin = new FileInputStream(arquivo);
            ObjectInputStream oin = new ObjectInputStream(fin);

            //Lendo objetos de um arquivo
            Jogador[] players= (Jogador[]) oin.readObject();
            oin.close();
            fin.close();

            for (Jogador p : players) {
                p.imprimirDados();//Ainda falta essa função aqui
            }
        } catch (Exception ex) {
            System.err.println("erro: " + ex.toString());
        }

    }

}