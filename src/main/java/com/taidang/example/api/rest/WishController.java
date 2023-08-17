package com.taidang.example.api.rest;

import com.taidang.example.api.rest.AbstractRestHandler;
import com.taidang.example.domain.Hotel;
import com.taidang.example.domain.Wish;
import com.taidang.example.exception.DataFormatException;
import com.taidang.example.service.WishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
 * Demonstrates how to set up RESTful API endpoints using Spring MVC
 */

@RestController
@RequestMapping(value = "/api/v1/wishes")
@Api(tags = {"wish"})
public class WishController extends AbstractRestHandler {

    @Autowired
    private WishService wishService;

    @RequestMapping(value = "",
            method = RequestMethod.POST,
            consumes = {"application/json", "application/xml"},
            produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create a wish resource.", notes = "Returns the URL of the new resource in the Location header.")
    public void create(@RequestBody Wish wish,
                                 HttpServletRequest request, HttpServletResponse response) {
        Wish created = this.wishService.create(wish);
        response.setHeader("Location", request.getRequestURL().append("/").append(created.getId()).toString());
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get a paginated list of all wishes.", notes = "The list is paginated. You can provide a page number (default 0) and a page size (default 100)")
    public
    @ResponseBody
    Page<Wish> getAll(@ApiParam(value = "The page number (zero-based)", required = true)
                                      @RequestParam(value = "page", required = true, defaultValue = DEFAULT_PAGE_NUM) Integer page,
                                      @ApiParam(value = "Tha page size", required = true)
                                      @RequestParam(value = "size", required = true, defaultValue = DEFAULT_PAGE_SIZE) Integer size,
                                      HttpServletRequest request, HttpServletResponse response) {
        return this.wishService.getAll(page, size);
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get a single.", notes = "You have to provide a valid ID.")
    public
    @ResponseBody
    Wish get(@ApiParam(value = "The ID of the wish.", required = true)
                             @PathVariable("id") Long id,
                             HttpServletRequest request, HttpServletResponse response) throws Exception {
        Wish hotel = this.wishService.get(id);
        checkResourceFound(hotel);
        //todo: http://goo.gl/6iNAkz
        return hotel;
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.PUT,
            consumes = {"application/json", "application/xml"},
            produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Update a hotel resource.", notes = "You have to provide a valid hotel ID in the URL and in the payload. The ID attribute can not be updated.")
    public void update(@ApiParam(value = "The ID of the existing hotel resource.", required = true)
                                 @PathVariable("id") Long id, @RequestBody Wish wish,
                                 HttpServletRequest request, HttpServletResponse response) {
        checkResourceFound(this.wishService.get(id));
        if (id != wish.getId()) throw new DataFormatException("ID doesn't match!");
        this.wishService.update(wish);
    }

    //todo: @ApiImplicitParams, @ApiResponses
    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE,
            produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Delete a wish resource.", notes = "You have to provide a valid wish ID in the URL. Once deleted the resource can not be recovered.")
    public void delete(@ApiParam(value = "The ID of the existing wish resource.", required = true)
                                 @PathVariable("id") Long id, HttpServletRequest request,
                                 HttpServletResponse response) {
        checkResourceFound(this.wishService.get(id));
        this.wishService.delete(id);
    }
}
