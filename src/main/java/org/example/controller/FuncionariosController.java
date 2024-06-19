package org.example.controller;

import org.example.dao.FuncionarioDAO;
import org.example.model.Funcionario;
import org.example.view.FuncionariosView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;

public class FuncionariosController {
    private final FuncionariosView funcionariosView;
    private final FuncionarioDAO funcionarioDAO;
    private Map<String, List<Funcionario>> funcionariosMap;

    public FuncionariosController() {
        funcionariosView = new FuncionariosView();
        funcionarioDAO = new FuncionarioDAO();
    }

    public void listOptions() {
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
                    showFuncionarios(funcionarioDAO.getAll());
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
            funcionarioDAO.create(new Funcionario("Maria", LocalDate.of(2000,10,18), new BigDecimal("2009.44"), "Operador"));
            funcionarioDAO.create(new Funcionario("João", LocalDate.of(1990,5,12), new BigDecimal("2284.38"), "Operador"));
            funcionarioDAO.create(new Funcionario("Caio", LocalDate.of(1961,5,2), new BigDecimal("9836.14"), "Coordenador"));
            funcionarioDAO.create(new Funcionario("Miguel", LocalDate.of(1988,10,14), new BigDecimal("19119.88"), "Diretor"));
            funcionarioDAO.create(new Funcionario("Alice", LocalDate.of(1995,1,5), new BigDecimal("2234.68"), "Recepcionista"));
            funcionarioDAO.create(new Funcionario("Heitor", LocalDate.of(1999,11,19), new BigDecimal("1582.72"), "Operador"));
            funcionarioDAO.create(new Funcionario("Arthur", LocalDate.of(1993,3,31), new BigDecimal("4071.84"), "Contador"));
            funcionarioDAO.create(new Funcionario("Laura", LocalDate.of(1994,7,8), new BigDecimal("3017.45"), "Gerente"));
            funcionarioDAO.create(new Funcionario("Heloísa", LocalDate.of(2003,5,24), new BigDecimal("1606.85"), "Eletricista"));
            funcionarioDAO.create(new Funcionario("Helena", LocalDate.of(1996,9,2), new BigDecimal("2799.93"), "Gerente"));
            funcionariosView.showSucessfullMessage();
        } catch (RuntimeException _) {
            funcionariosView.showErrorMessage("Algo deu errado. Tente novamente!");
        }
    }

    public void removeJoao(){
        try {
            funcionarioDAO.deleteByName("João");
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
            funcionarioDAO.increaseTenPercent();
            funcionariosView.showSucessfullMessage();
        } catch (Exception _) {
            funcionariosView.showErrorMessage("Algo deu errado. Tente novamente!");
        }
    }

    public void groupByFuncao(){
        try {
            funcionariosMap = new HashMap<>();
            funcionarioDAO
                    .getAll()
                    .forEach(funcionario -> {
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

        List<String> formattedFuncionarios = funcionariosMap
                .entrySet()
                .stream()
                .map(funcionario -> funcionario.getKey() + " = " + funcionario.getValue())
                .toList();

        funcionariosView.showPlainMessage(String.join("\n", formattedFuncionarios), null);
    }

    public void funcionariosFromMonth10or12(){
        showFuncionarios(funcionarioDAO.getWithMonth10or12());
    }

    public void biggerAge(){
        try {
            Funcionario funcionario = funcionarioDAO.getMoreOlder();
            int funcionarioAge = Period.between(funcionario.getDataNascimento(), LocalDate.now()).getYears();
            String message = "Funcionário: " + funcionario.getNome() + "\nIdade: " + funcionarioAge;
            funcionariosView.showPlainMessage(message, "Funcionário mais velho");
        } catch (NullPointerException e) {
            funcionariosView.showErrorMessage("Nenhum funcionário encontrado!");
        } catch (Exception _) {
            funcionariosView.showErrorMessage("Algo deu errado. Tente novamente!");
        }
    }

    public void orderByNome() {
        showFuncionarios(funcionarioDAO.getAllOrderedByName());
    }

    public void sumSalario() {
        try {
            BigDecimal total = funcionarioDAO.sumAllSalaries();
            DecimalFormat df = new DecimalFormat("#,###.00");
            funcionariosView.showPlainMessage("R$" + df.format(total), "Salário total");
        } catch (IllegalArgumentException e) {
            funcionariosView.showErrorMessage("Nenhum funcionário encontrado!");
        }
    }

    public void salarioMinimoQuantity(){
        Object[][] rows = funcionarioDAO
                .getAll()
                .stream()
                .map(funcionario -> new Object[]{funcionario.getNome(), funcionario.getSalario().divide(new BigDecimal("1212"), RoundingMode.HALF_UP)})
                .toArray(Object[][]::new);

        String[] cols = {"Nome", "Quantidade de salarios mínimos"};

        funcionariosView.showFuncionarios(cols, rows);
    }
}
