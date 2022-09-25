/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jason.filter;

import java.util.ArrayList;
import jason.asSyntax.Term;

public class Filter {

    private Operator operator;
    private ArrayList<String> predicates;
    private Term expression;

    public Filter() {
        predicates = new ArrayList<String>();
    }

    public Filter(Operator operator, ArrayList<String> predicates, Term expression) {
        this.operator = operator;
        this.predicates = predicates;
        this.expression = expression;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public ArrayList<String> getPredicate() {
        return predicates;
    }

    public void setPredicate(ArrayList<String> predicates) {
        this.predicates = predicates;
    }

    public void addPredicate(String predicate) {
        this.predicates.add(predicate);
    }

    public Term getExpression() {
        return expression;
    }

    public void setExpression(Term expression) {
        this.expression = expression;
    }

    @Override
    public String toString() {
        String internalAction = "[FILTER]: .filter(" + this.operator.getValue() + "," + this.predicates.get(0);
        if (this.expression != null) {
            internalAction += "," + this.expression.toString() + ")";
        } else {
            internalAction += ")";
        }
        return internalAction;
    }

}
