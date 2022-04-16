package com.seasongg.season.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class SeasonServiceTest {

	@Spy
	@InjectMocks
	private SeasonService seasonService;

	@Mock
	SeasonRepository seasonRepositoryMock;

	@Test
	void should_NotThrowException_When_SeasonIdOK() throws SeasonService.SeasonException {
		// given
		BigInteger validSeasonId = BigInteger.valueOf(1);

		// when
		seasonService.validateSeasonId(validSeasonId);

		// then no exception thrown

	}

	@Test
	void should_ThrowException_When_SeasonIdNull() {
		// given
		BigInteger nullSeasonId = null;
		doReturn("test-user").when(seasonService).getUsername();

		// when
		Executable executable = () -> seasonService.validateSeasonId(nullSeasonId);

		// then
		assertThrows(SeasonService.SeasonException.class, executable);

	}

}