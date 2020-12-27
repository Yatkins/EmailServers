package com.example.gmail;

import com.example.gmail.config.FeatureSwitchConfiguration;
import com.example.gmail.controller.MailController;
import com.example.gmail.model.GetUUID;
import com.example.gmail.model.Login;
import com.example.gmail.service.MailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.*;

@ExtendWith(MockitoExtension.class)
public class EmailControllerTest {
    @Mock
    private MailService mailService;

    @Mock
    private FeatureSwitchConfiguration featureSwitchConfiguration;

    @InjectMocks
    private MailController mailController;

    @Test
    public void login_returnsSuccess_whenEmailIsTurnedOn_andLoginIsSuccessful() throws Exception {
        GetUUID key = GetUUID.builder()
                .primaryKey(UUID.randomUUID())
                .build();

        Login userPass = Login.builder().build();

        when(featureSwitchConfiguration.isEmailUp()).thenReturn(true);
        when(mailService.loginToEmail(any(Login.class))).thenReturn(key.toString());

        ResponseEntity actual = mailController.login(userPass);

        verify(featureSwitchConfiguration).isEmailUp();
        verify(mailService).loginToEmail(userPass);

        assertThat(actual.getStatusCode()).isEqualTo(OK);
        assertThat(((GetUUID)actual.getBody()).getPrimaryKey()).isEqualTo(key.getPrimaryKey());
    }

    @Test
    public void login_returnsServerDown_whenEmailIsTurnedOff() throws Exception {
        when(featureSwitchConfiguration.isEmailUp()).thenReturn(false);

        ResponseEntity actual = mailController.login(Login.builder().build());

        verify(featureSwitchConfiguration).isEmailUp();

        assertThat(actual.getStatusCode()).isEqualTo(SERVICE_UNAVAILABLE);
        assertThat((String)actual.getBody()).isEqualTo("Service Down");
    }

    @Test
    public void login_returnsResponseWithException_whenEmailServiceThrowsHttpClientException() throws Exception {
        when(featureSwitchConfiguration.isEmailUp()).thenReturn(true);
        when(mailService.loginToEmail(any(Login.class))).thenThrow(new HttpClientErrorException(BAD_REQUEST));

        Login login = Login.builder().build();
        ResponseEntity actual = mailController.login(login);

        verify(featureSwitchConfiguration).isEmailUp();
        verify(mailService).loginToEmail(login);

        assertThat(actual.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat((String)actual.getBody()).isEqualTo(BAD_REQUEST.toString());
    }
}

//mock all three dependencies, (private final variables)
//test positive scenarios that log in worked. login returns success
//test if login returns an error
//test if feature switch is false, sservice unavailable

//basically mock everything then test all the possible returns to see
