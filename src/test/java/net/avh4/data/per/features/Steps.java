package net.avh4.data.per.features;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import net.avh4.data.per.ListRef;
import net.avh4.data.per.ListTransaction;
import net.avh4.data.per.RefRepository;
import net.avh4.data.per.service.InMemoryRefService;
import net.avh4.data.per.service.RefService;

import java.io.Serializable;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.fest.assertions.Assertions.assertThat;

@SuppressWarnings("UnusedDeclaration")
public class Steps {
    private RefService<Serializable> refService;
    private RefRepository<Serializable> refRepository;
    private Class<FarmAnimal> dataClass;
    private ListRef<FarmAnimal> listRef;
    private FarmAnimal addedData;

    @Given("^a storage service$")
    public void a_storage_service() throws Throwable {
        refService = new InMemoryRefService();
        refRepository = new RefRepository<Serializable>(refService);
    }

    @Given("^a data class definition$")
    public void a_data_class_definition() throws Throwable {
        dataClass = FarmAnimal.class;
    }

    @When("^I create a list ref for the data class$")
    public void I_create_a_list_ref_for_the_data_class() throws Throwable {
        checkNotNull(refRepository);
        checkNotNull(dataClass);
        listRef = new ListRef<FarmAnimal>(refRepository, "Farmyard", dataClass);
    }

    @When("^I add some data to the list ref$")
    public void I_add_some_data_to_the_list_ref() throws Throwable {
        checkNotNull(listRef);
        assertThat(listRef.content().size()).isEqualTo(0);

        addedData = new FarmAnimal("Bessie", 14);
        listRef.execute(new ListTransaction<FarmAnimal>() {
            @Override protected void mutate(List<FarmAnimal> mutableList) {
                mutableList.add(addedData);
            }
        });

        assertThat(listRef.content()).contains(addedData);
        assertThat(listRef.content().size()).isEqualTo(1);
    }

    @Then("^a new list ref from the same repository will contain the data$")
    public void a_new_list_ref_from_the_same_repository_will_contain_the_data() throws Throwable {
        checkNotNull(refService);
        checkNotNull(dataClass);
        checkNotNull(addedData);

        RefRepository newRepository = new RefRepository<Serializable>(refService);
        ListRef<FarmAnimal> newListRef = new ListRef<FarmAnimal>(newRepository, "Farmyard", dataClass);

        assertThat(newListRef.content()).contains(addedData);
    }
}
