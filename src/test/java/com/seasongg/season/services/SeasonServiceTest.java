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

	@Test
	void should_NotThrowException_When_SeasonNameOK() throws SeasonService.SeasonException {
		// given
		String validSeasonName = "Season Name 2022";

		// when
		seasonService.validateSeasonName(validSeasonName);

		// then no exception thrown

	}

	@Test
	void should_ThrowException_When_SeasonNameNull() {
		// given
		String nullSeasonName = null;

		// when
		Executable executable = () -> seasonService.validateSeasonName(nullSeasonName);

		// then
		assertThrows(SeasonService.SeasonException.class, executable);

	}

	@Test
	void should_ThrowException_When_SeasonNameEmpty() {
		// given
		String emptySeasonName = "";

		// when
		Executable executable = () -> seasonService.validateSeasonName(emptySeasonName);

		// then
		assertThrows(SeasonService.SeasonException.class, executable);

	}

	@Test
	void should_ThrowException_When_TrimmedSeasonNameTooShort() {
		// given
		String seasonName = "   A B   ";

		// when
		Executable executable = () -> seasonService.validateSeasonName(seasonName);

		// then
		assertThrows(SeasonService.SeasonException.class, executable);

	}

	@Test
	void should_ThrowException_When_SeasonNameTooLong() {
		// given
		String emptySeasonName = "this is a very very very very very very very long season name!";

		// when
		Executable executable = () -> seasonService.validateSeasonName(emptySeasonName);

		// then
		assertThrows(SeasonService.SeasonException.class, executable);

	}

	@Test
	void should_NotThrowException_When_CreateSeasonDatesOK() throws SeasonService.SeasonException {
		// given
		String validSeasonEndDate = "10/12/3000";

		// when
		seasonService.validateSeasonDates(validSeasonEndDate, false);

		// then no exception thrown

	}

	@Test
	void should_ThrowException_When_SeasonEndDateNull() {
		// given
		String nullSeasonEndDate = null;

		// when
		Executable executable = () -> seasonService.validateSeasonDates(nullSeasonEndDate, false);

		// then
		assertThrows(SeasonService.SeasonException.class, executable);

	}

	@Test
	void should_ThrowException_When_SeasonEndDateEmpty() {
		// given
		String emptySeasonEndDate = "";

		// when
		Executable executable = () -> seasonService.validateSeasonDates(emptySeasonEndDate, false);

		// then
		assertThrows(SeasonService.SeasonException.class, executable);

	}

	@Test
	void should_ThrowException_When_SeasonEndDateInvalidFormat() {
		// given
		String invalidSeasonEndDate = "3-12-2060";
		doReturn("test-user").when(seasonService).getUsername();

		// when
		Executable executable = () -> seasonService.validateSeasonDates(invalidSeasonEndDate, false);

		// then
		assertThrows(SeasonService.SeasonException.class, executable);

	}

	@Test
	void should_ThrowException_When_SeasonEndDateBeforeSeasonStartDate() {
		// given
		String invalidSeasonEndDate = "01/01/2000";

		// when
		Executable executable = () -> seasonService.validateSeasonDates(invalidSeasonEndDate, false);

		// then
		assertThrows(SeasonService.SeasonException.class, executable);

	}

	@Test
	void should_NotThrowException_When_GameIdOK() throws SeasonService.SeasonException {
		// given
		Integer validGameId = 1;

		// when
		seasonService.validateGameId(validGameId);

		// then no exception thrown

	}

	@Test
	void should_ThrowException_When_GameIdNull() {
		// given
		Integer nullGameId = null;
		doReturn("test-user").when(seasonService).getUsername();

		// when
		Executable executable = () -> seasonService.validateGameId(nullGameId);

		// then
		assertThrows(SeasonService.SeasonException.class, executable);

	}

}