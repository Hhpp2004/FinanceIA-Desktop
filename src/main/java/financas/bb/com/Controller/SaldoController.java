package financas.bb.com.Controller;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import financas.bb.com.Models.Categoria;
import financas.bb.com.Models.Extrato;
import financas.bb.com.Models.Operacao;
import financas.bb.com.Models.Saldo;
import financas.bb.com.Models.Tipo;
import financas.bb.com.Models.User;
import financas.bb.com.Repository.ExtratoRepositoy;
import financas.bb.com.Repository.OperacaoRepository;
import financas.bb.com.Repository.SaldoRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Transactional
@Service
public class SaldoController {

    private final SaldoRepository sr;
    private final ExtratoRepositoy er;
    private final OperacaoRepository or;

    public Saldo getSaldo(User user) {
        Saldo saldo = user.getSaldo();
        return saldo;
    }

    //Aqui pega o extrato de um mês especifico 
    public Extrato getExtrato(User user) {
        LocalDate now = LocalDate.now();
        Extrato extrato = er.findExtratoCompleto(user,now);

        return extrato;
    }

    private void atualizarSaldoUser(User user, Operacao operacao) {
        Saldo saldo = user.getSaldo();

        
        if (operacao.getTipo() == Tipo.ENTRADA) {
            saldo.setSaldo(saldo.getSaldo() + operacao.getValor());
            saldo.setValorEntrada(saldo.getValorEntrada() + operacao.getValor());
        } else {
            saldo.setSaldo(saldo.getSaldo() - operacao.getValor());
            saldo.setValorSaida(saldo.getValorSaida() + operacao.getValor());
        }

        sr.save(saldo);
    }

    private void atualizarSaldoExtrato(Extrato extrato, Operacao operacao) {
        Saldo saldo = extrato.getSaldo();
        if (saldo == null) {
            saldo = new Saldo();
            extrato.setSaldo(saldo);
        }

        if (operacao.getTipo() == Tipo.ENTRADA) {
            saldo.setSaldo(saldo.getSaldo() + operacao.getValor());
            saldo.setValorEntrada(saldo.getValorEntrada() + operacao.getValor());
        } else {
            saldo.setSaldo(saldo.getSaldo() - operacao.getValor());
            saldo.setValorSaida(saldo.getValorSaida() + operacao.getValor());
        }

        sr.save(saldo);
    }

    private String nomeExtrato(LocalDate data) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM / yyyy",  Locale.of("pt", "BR"));
        String nome = data.format(formatter);
        return nome.substring(0, 1).toUpperCase() + nome.substring(1);
    }

    private Extrato obterOuCriarExtrato(User user, LocalDate now) {
        String nome = nomeExtrato(now);
        return er.findByUserAndNome(user, nome)
                .orElseGet(() -> {
                    List<Extrato> aux = er.findLastExtratos(user);
                    if (!aux.isEmpty()) {
                        Saldo saldoAux = aux.get(0).getSaldo();
                        Extrato e = new Extrato();
                        e.setNome(nome);
                        e.setUser(user);
                        e.setComp(now);
                        e.setSaldo(new Saldo(0.00, 0.00, saldoAux.getSaldo() == null ? 0.00 : saldoAux.getSaldo()));
                        e.setOperacao(new ArrayList<>());
                        er.save(e);
                        return e;
                    }
                else {
                        Extrato e = new Extrato();
                        e.setNome(nome);
                        e.setUser(user);
                        e.setComp(now);
                        e.setSaldo(new Saldo());
                        e.setOperacao(new ArrayList<>());
                        er.save(e);
                        return e;
                    }                    
                });
    }

    public Extrato createExtrato(User user, LocalDate now)  {
        List<Extrato> lastExtrato = er.findLastExtratos(user);
        if (!lastExtrato.isEmpty()) {
            if (LocalDate.now().isAfter(now)) {
                if (!er.existsByComp(now)) {
                    Saldo ultimoSaldo = lastExtrato.get(0).getSaldo();
                    Extrato e = new Extrato(nomeExtrato(now),
                            new Saldo(0.00, 0.00, ultimoSaldo == null ? 0.00 : ultimoSaldo.getSaldo()), now,
                            new ArrayList<>());
                    return e;
                }
                else {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    //Aqui é criado a operação de transações que o cliente faz durante o mês. A logica é cria a operação, registra no extrato e atualiza o saldo do extrato do mês referente e o saldo do usuario geral apresentado no dashboard
    public Operacao createOperacao(String nome, Double valor, String desc, LocalDate data ,Categoria categoria, Tipo tipo, User user) {
        Operacao operacao = new Operacao(nome, valor, data, desc, categoria, tipo);
        or.save(operacao);
    
        Extrato extrato = obterOuCriarExtrato(user, data);
        extrato.getOperacao().add(operacao);
        extrato.getOperacao().forEach(e -> System.out.println(e));
        atualizarSaldoExtrato(extrato, operacao);     
        atualizarSaldoUser(user, operacao);
        return operacao;
    }

    public Extrato getExtratoUUID(UUID id) {
        return er.findByIdWithOperacoes(id).get();
    }
    
    @Transactional(readOnly = true)
    public List<Extrato> buscarExtratoComOperacoes(Saldo saldo, String nome) {
        return er.findComOperacoes(saldo, nome);
    }

    //Toda vez que cria uma operação, ela é registrada no extrato do mês referente e o saldo que é apresentado na tela de dashboard é atualizado. Esse saldo é geral e tem relação ao model user
    public void updateSaldo(Extrato extrato, User user) {
        if (!extrato.getOperacao().isEmpty()) {
            Operacao operacao = extrato.getOperacao().get(0);
            Saldo saldo = user.getSaldo();
            if (operacao.getTipo() == Tipo.ENTRADA) {
                saldo.setSaldo(saldo.getSaldo() + operacao.getValor());
                saldo.setValorEntrada(saldo.getValorEntrada() + operacao.getValor());
                sr.save(saldo);
            } else if (operacao.getTipo() == Tipo.SAIDA) {
                saldo.setValorSaida(operacao.getValor() + saldo.getValorSaida());
                saldo.setSaldo(saldo.getSaldo() - operacao.getValor());
                sr.save(saldo);
            }
        }
    }

    //Para criar um novo extrato, o extrato é uma lista no model user, pois ele tem mais de um extrato referente ao mês para criar um historico 
    public void createExtrato(LocalDate now, User user, Operacao operacao) {
        
        Extrato extrato = er.findByUserAndComp(user, now)
                .orElseGet(() -> {
                    Extrato novo = new Extrato();
                    novo.setNome(nomeExtrato(now));
                    novo.setUser(user);
                    novo.setComp(now);
                    novo.setOperacao(new ArrayList<>());

                    Saldo saldoExtrato = new Saldo(0.0, 0.0, 0.0);
                    sr.save(saldoExtrato); // 👈 SALVA PRIMEIRO
                    novo.setSaldo(saldoExtrato);
                    er.save(novo);
                    return novo;
                });

        Saldo saldo = extrato.getSaldo();
        Double valor = operacao.getValor();

        if (operacao.getTipo() == Tipo.ENTRADA) {
            saldo.setSaldo(saldo.getSaldo() + valor);
            saldo.setValorEntrada(saldo.getValorEntrada() + valor);
        } else {
            saldo.setSaldo(saldo.getSaldo() - valor);
            saldo.setValorSaida(saldo.getValorSaida() + valor);
        }

        sr.save(saldo);

        extrato.getOperacao().add(operacao);

        er.save(extrato);
    }


    //Calculo para mostrar a porcentagem no grafico pizza
    public Double calculoPorcentagem(Extrato extrato, Categoria categoria) {

        double totalSaidas = extrato.getOperacao().stream()
                .filter(o -> o.getTipo() == Tipo.SAIDA)
                .mapToDouble(Operacao::getValor)
                .sum();

        if (totalSaidas == 0)
            return 0.0;

        double categoriaTotal = extrato.getOperacao().stream()
                .filter(o -> o.getTipo() == Tipo.SAIDA)
                .filter(o -> o.getCategoria() == categoria)
                .mapToDouble(Operacao::getValor)
                .sum();

        return (categoriaTotal * 100) / totalSaidas;
    }
}
