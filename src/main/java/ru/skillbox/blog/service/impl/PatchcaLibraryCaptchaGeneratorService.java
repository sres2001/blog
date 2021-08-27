package ru.skillbox.blog.service.impl;

import org.patchca.color.SingleColorFactory;
import org.patchca.filter.predefined.CurvesRippleFilterFactory;
import org.patchca.font.RandomFontFactory;
import org.patchca.service.Captcha;
import org.patchca.service.ConfigurableCaptchaService;
import org.patchca.word.RandomWordFactory;
import org.springframework.stereotype.Service;
import ru.skillbox.blog.dto.GeneratedCaptchaDto;
import ru.skillbox.blog.service.CaptchaGeneratorService;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class PatchcaLibraryCaptchaGeneratorService implements CaptchaGeneratorService {

    private final ConfigurableCaptchaService captchaService;

    public PatchcaLibraryCaptchaGeneratorService() {
        captchaService = new ConfigurableCaptchaService();
        captchaService.setWidth(85);
        captchaService.setHeight(30);
        RandomWordFactory wordFactory = new RandomWordFactory();
        wordFactory.setCharacters("abcdekpqstvxyz2345678");
        wordFactory.setMinLength(4);
        wordFactory.setMaxLength(4);
        captchaService.setWordFactory(wordFactory);
        captchaService.setColorFactory(new SingleColorFactory(new Color(25, 60, 170)));
        captchaService.setFilterFactory(new CurvesRippleFilterFactory(captchaService.getColorFactory()));
        RandomFontFactory fontFactory = new RandomFontFactory();
        fontFactory.setMinSize(24);
        fontFactory.setMaxSize(24);
        captchaService.setFontFactory(fontFactory);
    }

    @Override
    public GeneratedCaptchaDto generateCaptcha() {
        Captcha captcha = captchaService.getCaptcha();
        BufferedImage bufferedImage = captcha.getImage();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            if (!ImageIO.write(bufferedImage, "png", baos)) {
                throw new RuntimeException("Cannot generate captcha: no appropriate writer");
            }
        } catch (IOException e) {
            throw new RuntimeException("Cannot generate captcha", e);
        }
        GeneratedCaptchaDto dto = new GeneratedCaptchaDto();
        dto.setId(RandomUtils.generateCaptchaSecret());
        dto.setCode(captcha.getChallenge());
        dto.setPngImage(baos.toByteArray());
        return dto;
    }
}
