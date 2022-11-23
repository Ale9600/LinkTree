package base.utility.module.utilities.singleton.controllers;


import base.utility.module.utilities.responses.ResponseUtility;
import jakarta.inject.Inject;


public abstract class BasicCrudController {

    @Inject
    public ResponseUtility responseUtility;

}