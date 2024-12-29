package br.com.actionlabs.carboncalc.rest;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.actionlabs.carboncalc.dto.ServerStatusDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/status")
@RequiredArgsConstructor
@Slf4j
public class StatusRestController {

    @Value("${server.version}")
    private String version;

    @ResponseBody
    @GetMapping("/check")
    public ServerStatusDTO checkStatus() {
        log.info("Checking application health status...");
        long currentTimeMillis = System.currentTimeMillis();
        return new ServerStatusDTO(version, currentTimeMillis, new Date(currentTimeMillis).toString());
    }

}
