package org.example.controller;

import org.example.model.Funcionario;
import org.example.view.FuncionariosView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;

public class FuncionariosController {
    private final List<Funcionario> funcionarios;
    private final FuncionariosView funcionariosView;
    private Map<String, List<Funcionario>> funcionariosMap;

    public FuncionariosController() {
        funcionarios = new ArrayList<>();
        funcionariosView = new FuncionariosView();
    }

    public void listOptions () {
        while(true) {
            int option = funcionariosView.options();

            switch (option) {
                case -1:
                    return;
                case 0:
                    insertAll();
                    break;
                case 1:
                    removeJoao();
                    break;
                case 2:
                    showFuncionarios(funcionarios);
                    break;
                case 3:
                    increaseTenPercent();
                    break;
                case 4:
                    groupByFuncao();
                    break;
                case 5:
                    funcionariosByFuncao();
                    break;
                case 6:
                    funcionariosFromMonth10or12();
                    break;
                case 7:
                    biggerAge();
                    break;
                case 8:
                    orderByNome();
                    break;
                case 9:
                    sumSalario();
                    break;
                case 10:
                    salarioMinimoQuantity();
                    break;
                default:
                    funcionariosView.showErrorMessage("Insira uma opção válida!");
            }
        }
    }

    public void insertAll() {
        try{
            Collections.addAll(
                    funcionarios,
                    new Funcionario("Maria", LocalDate.of(2000,10,18), new BigDecimal("2009.44"), "Operador"),
                    new Funcionario("João", LocalDate.of(1990,5,12), new BigDecimal("2284.38"), "Operador"),
                    new Funcionario("Caio", LocalDate.of(1961,5,2), new BigDecimal("9836.14"), "Coordenador"),
                    new Funcionario("Miguel", LocalDate.of(1988,10,14), new BigDecimal("19119.88"), "Diretor"),
                    new Funcionario("Alice", LocalDate.of(1995,1,5), new BigDecimal("2234.68"), "Recepcionista"),
                    new Funcionario("Heitor", LocalDate.of(1999,11,19), new BigDecimal("1582.72"), "Operador"),
                    new Funcionario("Arthur", LocalDate.of(1993,3,31), new BigDecimal("4071.84"), "Contador"),
                    new Funcionario("Laura", LocalDate.of(1994,7,8), new BigDecimal("3017.45"), "Gerente"),
                    new Funcionario("Heloísa", LocalDate.of(2003,5,24), new BigDecimal("1606.85"), "Eletricista"),
                    new Funcionario("Helena", LocalDate.of(1996,9,2), new BigDecimal("2799.93"), "Gerente")
            );
            funcionariosView.showSucessfullMessage();
        } catch (Exception _) {
            funcionariosView.showErrorMessage("Algo deu errado. Tente novamente!");
        }
    }

    public void removeJoao(){
        try{
            funcionarios.removeIf(funcionario -> funcionario.getNome().equals("João"));
            funcionariosView.showSucessfullMessage();
        } catch (Exception _) {
            funcionariosView.showErrorMessage("Algo deu errado. Tente novamente!");
        }
    }

    public void showFuncionarios(List<Funcionario> funcionarios){
        Object[][] rows = funcionarios
                .stream()
                .map(funcionario -> new Object[]{funcionario.getNome(), funcionario.getFormattedDataNascimento(), funcionario.getFormattedSalario(), funcionario.getFuncao()})
                .toArray(Object[][]::new);

        String[] cols = {"Nome", "Data de nascimento", "Salário", "Função"};

        funcionariosView.showFuncionarios(cols, rows);
    }

    public void increaseTenPercent(){
        try {
            funcionarios.forEach(funcionario -> {
                BigDecimal tenPercent = funcionario.getSalario().multiply(new BigDecimal("0.10"));
                BigDecimal newSalario = funcionario.getSalario().add(tenPercent);
                funcionario.setSalario(newSalario);
            });
            funcionariosView.showSucessfullMessage();
        } catch (Exception _) {
            funcionariosView.showErrorMessage("Algo deu errado. Tente novamente!");
        }
    }

    public void groupByFuncao(){
        try {
            funcionariosMap = new HashMap<>();
            funcionarios.forEach(funcionario -> {
                String key = funcionario.getFuncao();

                if (funcionariosMap.containsKey(key)) {
                    funcionariosMap.get(key).add(funcionario);
                    return;
                }

                funcionariosMap.put(key, new ArrayList<>());
                funcionariosMap.get(key).add(funcionario);
            });
            funcionariosView.showSucessfullMessage();
        } catch (Exception _) {
            funcionariosView.showErrorMessage("Algo deu errado. Tente novamente!");
        }
    }

    public void funcionariosByFuncao() {
        if (funcionariosMap == null || funcionariosMap.isEmpty()) {
            funcionariosView.showErrorMessage("Os funcionários não foram agrupados ou não existem. Tente novamente após realizar estas operações!");
            return;
        }
        List<String> formattedFuncionarios = funcionariosMap.entrySet().stream().map(funcionario -> funcionario.getKey() + " = " + funcionario.getValue()).toList();
        funcionariosView.showPlainMessage(String.join("\n", formattedFuncionarios), null);
    }

    public void funcionariosFromMonth10or12(){
        List<Funcionario> filteredFuncionarios = funcionarios
                .stream()
                .filter(funcionario -> funcionario.getDataNascimento().getMonthValue() == 10 || funcionario.getDataNascimento().getMonthValue() == 12)
                .toList();

        showFuncionarios(filteredFuncionarios);
    }

    public void biggerAge(){
        funcionarios
                .stream()
                .min(Comparator.comparing(Funcionario::getDataNascimento))
                .ifPresentOrElse(funcionario -> {
                    int moreOlderFuncionarioAge = Period.between(funcionario.getDataNascimento(), LocalDate.now()).getYears();
                    String message = "Funcionário: " + funcionario.getNome() + "\nIdade: " + moreOlderFuncionarioAge;

                    funcionariosView.showPlainMessage(message, "Funcionário mais velho");
                }, () -> funcionariosView.showErrorMessage("Nenhum funcionário encontrado!"));
    }

    public void orderByNome() {
        List<Funcionario> orderedFuncionarios = new ArrayList<>(List.copyOf(funcionarios));
        orderedFuncionarios.sort(Comparator.comparing(Funcionario::getNome));
        showFuncionarios(orderedFuncionarios);
    }

    public void sumSalario() {
        BigDecimal total = funcionarios.stream().reduce(new BigDecimal("0"), (acc, elem) -> acc.add(elem.getSalario()), BigDecimal::add);
        DecimalFormat df = new DecimalFormat("#,###.00");
        funcionariosView.showPlainMessage("R$" + df.format(total), "Salário total");
    }

    public void salarioMinimoQuantity(){
        Object[][] rows = funcionarios
                .stream()
                .map(funcionario -> new Object[]{funcionario.getNome(), funcionario.getSalario().divide(new BigDecimal("1212"), RoundingMode.HALF_UP)})
                .toArray(Object[][]::new);

        String[] cols = {"Nome", "Quantidade de salarios mínimos"};

        funcionariosView.showFuncionarios(cols, rows);
    }
}
