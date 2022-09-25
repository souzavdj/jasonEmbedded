/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jason.filter;

public enum Operator {
    EXCEPT("except"), ONLY("only"), REMOVE("remove"), VALUE("value"), ALL("all");

    private String value;

    public String getValue() {
        return value;
    }

    Operator(String value) {
        this.value = value;
    }

    public static Operator fromString(String value) {
        for (Operator o : Operator.values()) {
            if (o.value.equalsIgnoreCase(value)) {
                return o;
            }
        }
        return null;
    }

}
