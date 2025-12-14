package com.chvs.documentconsumer;

import com.chvs.documentconsumer.in.document.DocumentConsumer;
import com.chvs.documentproducerapi.DocumentInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("test")
public class TestController {

    private final DocumentConsumer documentConsumer;

    @PostMapping
    public void test(@RequestBody List<DocumentInfo> documentsInfo) {
        documentConsumer.consume(documentsInfo);
    }
}
