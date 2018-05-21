package integration.com;

import java.beans.Transient;

import cucumber.api.java.en.Given;


public class Test {
    /**
     *
     * @param args
     */
    @Transient
    @Given("I am writing cucumber code")
    public void main(String[] args) {
    }

    /**
     *
     * @param z  This should be fourth
     * @param x  This should be second
     * @param w  This should be first
     * @since
     */
    @Given("^X (\\d) Mapping for (.*)$")
    public void main2(int w, String x, int y, int z) {
    }
}
