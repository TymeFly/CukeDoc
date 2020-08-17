/**
 * Created by Roger on 06/01/18.
 */
package integration.com.child;

import cucumber.api.java.en.And;
import cucumber.api.java.en.When;


/**
 * Class 2 comment
 * @since 1.2.3.4
 */
public class Test2 {
    /**
     * Hello World. This is a comment
     * @param args
     * @since 1.2.3
     */
    @Deprecated
    @And("Some cucumber code")
    @When("I Write some more cucumber code")
    public void main(String[] args) {
    }

    /**
     * Second method comment
     * @param args  more parameters
     * @since 1.2.3.4
     */
    @Deprecated
    public void main2(String args) {
    }

    /**
     * comment number 3
     * @param args
     * @since 1.2.3.4
     */
    @Deprecated
    @And("Mapping number 3")
    public void main3() {
    }

    /**
     * comment number 4
     * @param args
     */
    @Deprecated
    @And("Mapping number 4")
    public void main4() {
    }

    /**
     * comment number 5
     * @param args
     */
    @Deprecated
    @And("Mapping number 5")
    public void main5() {
    }

    /**
     * comment number 6
     * @param args
     */
    @Deprecated
    @And("Mapping number 6")
    public void main6() {
    }

    /**
     * comment number 7
     * @param args
     */
    @Deprecated
    @And("Mapping number 7")
    public void main7() {
    }


    @Given("^(\\w+ )?tables? (\".*?\"(?:(?:, ?| and )\".*?\")*) (?:is|are) empty$")
    public testComplex(@Nonnull String type, @Nonnull String names) {
    }
}
