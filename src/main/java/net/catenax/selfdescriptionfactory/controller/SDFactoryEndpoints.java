package net.catenax.selfdescriptionfactory.controller;

import com.danubetech.verifiablecredentials.VerifiableCredential;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.catenax.selfdescriptionfactory.dto.SDDocumentDto;
import net.catenax.selfdescriptionfactory.service.SDFactory;
import net.catenax.selfdescriptionfactory.util.BeanAsMap;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.HashMap;

@RestController
@RequestMapping("selfdescription")
@RequiredArgsConstructor
@Slf4j
public class SDFactoryEndpoints {

    private final SDFactory sdFactory;

    @PostMapping(consumes = {"application/json"}, produces = {"application/vc+ld+json"})
    @ResponseStatus(HttpStatus.CREATED)
    public VerifiableCredential publishSelfDescription(@RequestBody SDDocumentDto sdDocumentDto) throws Exception {
        var sdMap = new HashMap<>(BeanAsMap.asMap(sdDocumentDto));
        sdMap.remove("did");
        var verifiedCredentials = sdFactory.createVC(sdMap, URI.create(sdDocumentDto.getDid()));
        sdFactory.storeVC(verifiedCredentials);
        return verifiedCredentials;
    }

    @PostMapping(value = "/vc", consumes = {"application/vc+ld+json"})
    public void publishSelfDescription(@RequestBody VerifiableCredential verifiableCredential) throws Exception {
        sdFactory.storeVCWithCheck(verifiableCredential);
    }

}
