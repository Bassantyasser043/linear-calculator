package Evaluation;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationServices {
    public ValidationServices(String equations, int var) {
        this.equations = equations;
        data = new LinkedHashSet<String>();
        pos_plus = new ArrayList<Integer>();
        con = new ArrayList<Double>();
        eql = true;
        sent = "";
        this.var = var;
        split(equations);
    }

    ////////left side of the equation
    public String equations;
    public String[] eq;
    public String[] temp;
    public LinkedHashSet<String> data;
    private ArrayList<Integer> pos_plus;
    public ArrayList<Double> con;
    int var;
    private boolean eql;
    ///right side of the equation which must have the constants only
    public String[] constants;
    public String sent;

    ////////////////////////////////split equations
    public boolean split(String equations) {
        sent = equations.replaceAll("--", "+").replaceAll("([a-zA-Z]\\.)", "xx").replaceAll("[' ']+","");
        eq = sent.lines().toArray(String[]::new); //split equation entered in the box into arrays`
        temp = sent.lines().toArray(String[]::new);
        boolean valid = true;
        constants = sent.lines().toArray(String[]::new);
        try {
            //split right side of the equation and left side
            for (int i = 0; i < eq.length; i++) {
                constants[i] = (eq[i].substring(eq[i].lastIndexOf("=") + 1, eq[i].length()));
                eq[i] = eq[i].substring(0, eq[i].lastIndexOf("="));
                if (eq[i].indexOf("+") == 0) {
                    eq[i] = eq[i].substring(1, eq[i].length());
                }
                if (constants[i].indexOf("+") == 0) {
                    constants[i] = constants[i].substring(1, constants[i].length());
                }

            }
            ;
        } catch (Exception e) {
            //valid=false;
            return false;
        }
        return valid;
    }

    ////NUMBER OF EQUATION=VARIABLES
    public boolean checknumber() {
        if (eq.length == var) {
            //make sure that number of equations=number of unknowns
            return true;
        } else return false;
    }

    ////////////////////equation to verify the format of the equation
    private boolean checkformat(String[] eq) {
        boolean valid = false;

        if (checknumber()) {
            for (int j = 0; j < eq.length; j++) {
                for (int i = 0; i < eq[j].length(); i++) {
                    //make sure that character is not alphabet or number or operator (+ or -) or decimal point
                    if ((Character.compare(eq[j].charAt(i), '.') == 0) || (Character.isLetter(eq[j].charAt(i))) ||
                            (Character.isDigit(eq[j].charAt(i))) || (Character.compare(eq[j].charAt(i), ' ')) == 1 || (Character.compare(eq[j].charAt(i), '+') == 0
                            || (Character.compare(eq[j].charAt(i), '-') == 0))) {
                        valid = true;
                    } else {
                        valid = false;
                        break;
                    }
                }
                if (!valid) break;
            }
        }
        return valid;
    }

    /////////////////////validate same variables
    private boolean checksame(LinkedHashSet equation) {
        if (equation.size() > var) {
            return false;
        } else {
            return true;
        }
    }

    ////////////////////////////////////////check equality and plus signs repeatation
    private boolean check(String equation) {
        boolean valid = true;
        String line="([\\+]+){2}"; ///more than one plus
        String equaliate="([\\+])$";
        String equaliate2="(-\\+)";
        Pattern r = Pattern.compile(line);
        Pattern plus= Pattern.compile(equaliate);
        Pattern equal = Pattern.compile("(['=']+){2}");
        Pattern invalid = Pattern.compile(equaliate2);
        Matcher m = r.matcher(equation);
        Matcher what = invalid.matcher(equation);
        for (int i = 0; i < temp.length; i++) {
            Matcher sub = equal.matcher(equation);
            Matcher sub2 = plus.matcher(equation);
            if (temp[i].contains("=") && (temp[i].indexOf('=') != 0) && (temp[i].indexOf('=') != temp[i].length() - 1) && !sub.find() && temp[i].indexOf("=-") != temp[i].length() - 2 &&
                    temp[i].indexOf("=+") != temp[i].length() - 2&&!sub2.find())
                eql = true;
            else {
                eql = false;
                break;
            }

        }
        if (isValid(sent)) {
            if (m.find()||what.find()) {
                valid = false;
            } else {
                valid = true;
            }
        }
        return valid && eql;
    }

    //////////////////////////////////validity of the variables if it has more than one character
    private boolean isValid(String equations) {
        boolean valid = true;
        equations = equations.replaceAll("\\d", "").replaceAll("-", "").replaceAll("0", "");
        if (checkformat(eq) && checkformat(constants)) {
            String[] eq = equations.lines().toArray(String[]::new); //split equation entered in the box into arrays`
            for (int i = 0; i < eq.length; i++) {
                for (int j = 0; j < eq[i].length(); j++) {
                    if ((Character.compare(eq[i].charAt(j), '=') == 0))
                        eq[i] = eq[i].replace(String.valueOf(eq[i].charAt(j)), "+");
                    if (eq[i].indexOf("+") == 0) {
                        eq[i] = eq[i].substring(1, eq[i].length());
                    }
                }
            }
            for (int i = 0; i < eq.length; i++) {
                String[] temp = eq[i].split("\\+");
                int size = 0;
                for (int j = 0; j < temp.length; j++) {
                    if (temp[j].contains(".") || temp[j].contains("0")) size = temp[j].length() - 1;
                    else size = temp[j].length();
                    if (size > 1) {
                        valid = false;
                        break;
                    }
                }
                if (!valid) break;
            }
        } else {
            valid = false;
        }
        return valid;
    }

    /////////////////////////////////take variables of the equations
    /////we must make sure that equations entered in the right format
    private boolean variables(int var) {
        ArrayList<Integer> pos_variable = new ArrayList<Integer>();
        String temp = "";
        for (int i = 0; i < var; i++) {
            for (int j = 0; j < eq[i].length(); j++) {
                if ((Character.compare(eq[i].charAt(j), '+') == 0)) {
                    pos_plus.add(j);
                } else if (Character.isLetter(eq[i].charAt(j))) {
                    pos_variable.add(j);
                }
            }
            ///get variables till the last one
           // if(pos_plus.isEmpty()) continue;
            int couns = pos_plus.size();
            if (pos_plus.size() > pos_variable.size()) {
                couns = pos_variable.size();
            }
            for (int k = 0; k < couns; k++) {
                int start = pos_variable.get(k);
                int end = (int) pos_plus.get(k);
                if (pos_variable.get(k) > (int) pos_plus.get(k)) {
                    end = pos_variable.get(k);
                    start = pos_plus.get(k);
                }
                temp = eq[i].substring(start, end);
                /////////////////////
                if (!temp.isEmpty() && !temp.contains("+"))
                    data.add(temp);
                temp = "";
            }
            ///get last variable
            temp = eq[i].substring(pos_variable.get(pos_variable.size() - 1), eq[i].length());
            if (temp.contains("+")) {
                temp = eq[i].substring(pos_variable.get(pos_variable.size() - 1), eq[i].lastIndexOf("+"));
            }
            data.add(temp);
            temp = "";

            pos_plus.clear();
            pos_variable.clear();
        }
        System.out.print(data);
        return checksame(data) && check(sent)&&isValid(sent);
    }

    ////////////////////////////
    public boolean validation() {
        boolean valid = true;
        if (checkformat(constants) && checkformat(eq) && variables(var)) valid = true;
        else valid = false;
        return valid;
    }

}
