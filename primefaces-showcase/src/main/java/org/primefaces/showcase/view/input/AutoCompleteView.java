/*
 * The MIT License
 *
 * Copyright (c) 2009-2023 PrimeTek Informatics
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.primefaces.showcase.view.input;

import javax.annotation.PostConstruct;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.showcase.domain.Country;
import org.primefaces.showcase.domain.Customer;
import org.primefaces.showcase.service.CountryService;
import org.primefaces.showcase.service.CustomerService;
import org.primefaces.showcase.view.data.datatable.LazyCustomerDataModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Named
@ViewScoped
public class AutoCompleteView implements Serializable {
    private TextDTO textDTO;
    private CountryDTO countryDTO;
    private List<Country> selectedCountries;
    private LazyDataModel<Customer> lazyModel;

    @Inject
    private CountryService countryService;

    @Inject
    private CustomerService service;

    @Inject
    private AutoCompleteEventHandler eventHandler;

    @PostConstruct
    public void init() {
        textDTO = new TextDTO();
        countryDTO = new CountryDTO();
        lazyModel = new LazyCustomerDataModel(service.getCustomers(200));
    }

    // Getters and setters for textDTO, countryDTO, selectedCountries, and lazyModel

    public List<String> completeText(String query) {
        String queryLowerCase = query.toLowerCase();
        List<String> countryList = new ArrayList<>();
        List<Country> countries = countryService.getCountries();
        for (Country country : countries) {
            countryList.add(country.getName());
        }
        return countryList.stream().filter(t -> t.toLowerCase().startsWith(queryLowerCase)).collect(Collectors.toList());
    }

    public List<String> noResults(String query) {
        return Collections.emptyList();
    }

    public List<Country> completeCountry(String query) {
        String queryLowerCase = query.toLowerCase();
        List<Country> countries = countryService.getCountries();
        return countries.stream().filter(t -> t.getName().toLowerCase().contains(queryLowerCase)).collect(Collectors.toList());
    }

    public void onItemSelect(SelectEvent<String> event) {
        eventHandler.onItemSelect(event);
    }

    public void onEmptyMessageSelect() {
        eventHandler.onEmptyMessageSelect();
    }

    public void onMoreTextSelect() {
        eventHandler.onMoreTextSelect();
    }

    public void setCountryService(CountryService countryService) {
        this.countryService = countryService;
    }

    public char getCountryGroup(Country country) {
        return country.getName().charAt(0);
    }

}
