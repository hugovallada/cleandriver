package com.github.hugovallada;

import java.util.UUID;

import org.jboss.logging.annotations.Param;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/accounts")
public class AccountResource {
    private final AccountService accountService;

    public AccountResource(AccountService accountService) {
        this.accountService = accountService;
    }

    @POST
    public Response signup(AccountRequest input) {
        return Response.status(Response.Status.CREATED)
                .entity(accountService.signup(input))
                .build();
    }

    @GET
    @Path("/{accountId}")
    public Response getAccount(@Param UUID accountId) {
        return Response.ok(accountService.getAccount(accountId)).build();
    }
}
