package com.seasongg.season.services;

import com.seasongg.season.models.Season;
import com.seasongg.season.models.SeasonEditRequest;
import com.seasongg.season.models.SeasonResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SeasonEditServiceTest {

	@Spy
	@InjectMocks
	private SeasonEditService seasonEditService;

	@Mock
	SeasonRepository seasonRepository;

	@Test
	void should_EditSeason_When_InputOK() throws Exception {
		// given
		doNothing().when(seasonEditService).validateSeasonId(BigInteger.ONE);

		SeasonEditRequest seasonEditRequest = new SeasonEditRequest(
				BigInteger.ONE, "Season Name", "01/01/3000");

		Season seasonToEdit = new Season();
		seasonToEdit.setSeasonId(seasonEditRequest.getSeasonId());
		seasonToEdit.setName(seasonEditRequest.getSeasonName());

		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date parsedDate = dateFormat.parse(seasonEditRequest.getSeasonEndDate());

		seasonToEdit.setEndDate(new Timestamp(parsedDate.getTime()));

		doReturn(seasonToEdit).when(seasonEditService).findSeason(BigInteger.ONE);

		doNothing().when(seasonEditService).changeSeasonEndDate(any(Season.class), anyString());
		doNothing().when(seasonEditService).changeSeasonName(any(SeasonEditRequest.class), any(Season.class));

		SeasonResponse expectedSeasonResponse = new SeasonResponse(seasonToEdit);

		// when
		SeasonResponse actual = seasonEditService.editSeason(seasonEditRequest);

		// then
		assertEquals(expectedSeasonResponse, actual);

	}

	@Test
	void should_ThrowException_When_NoEditsOccurred() throws Exception {
		// given
		doNothing().when(seasonEditService).validateSeasonId(BigInteger.ONE);

		SeasonEditRequest seasonEditRequest = new SeasonEditRequest(
				BigInteger.ONE, "", "");

		doReturn(new Season()).when(seasonEditService).findSeason(BigInteger.ONE);

		// when
		Throwable exception = assertThrows(SeasonService.SeasonException.class, () -> {
			seasonEditService.editSeason(seasonEditRequest);
		});

		// then
		assertEquals("No changes were provided, so the season was not edited.", exception.getMessage());

	}

	@Test
	void should_SetSeasonDate() throws Exception {
		// given
		doNothing().when(seasonEditService).validateSeasonDates(anyString(), anyBoolean());
		doNothing().when(seasonEditService).adjustSeasonStatusIfNeeded(any(Season.class));

		Season seasonToEdit = new Season();

		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date parsedDate = dateFormat.parse("01/01/3000");

		doReturn(new Timestamp(parsedDate.getTime())).when(seasonEditService).getTimestamp(any());

		// when
		seasonEditService.changeSeasonEndDate(seasonToEdit, "01/01/3000");

		// then
		assertEquals(new Timestamp(parsedDate.getTime()), seasonToEdit.getEndDate());

	}

	@Test
	void should_SetSeasonActive_WhenSeasonHasNotEnded() throws Exception {
		// given
		Season seasonToEdit = new Season();

		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date parsedDate = dateFormat.parse("01/01/3000");

		seasonToEdit.setEndDate(new Timestamp(parsedDate.getTime()));

		// when
		seasonEditService.adjustSeasonStatusIfNeeded(seasonToEdit);

		// then
		assertEquals("A", seasonToEdit.getStatus());

	}

	@Test
	void should_SetSeasonInactive_WhenSeasonHasEnded() throws Exception {
		// given
		Season seasonToEdit = new Season();

		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date parsedDate = dateFormat.parse("01/01/2000");

		seasonToEdit.setEndDate(new Timestamp(parsedDate.getTime()));

		// when
		seasonEditService.adjustSeasonStatusIfNeeded(seasonToEdit);

		// then
		assertEquals("I", seasonToEdit.getStatus());

	}

	@Test
	void should_ValidateSeasonName_WhenSeasonNameChanged() throws Exception {
		// given
		doNothing().when(seasonEditService).validateSeasonName(anyString());

		Season seasonToEdit = new Season();
		SeasonEditRequest seasonEditRequest = new SeasonEditRequest(BigInteger.ONE, "Edited Season Name",
				"01/01/3000");

		given(seasonRepository.findByNameIgnoreCase(anyString())).willReturn(null);

		// when
		seasonEditService.changeSeasonName(seasonEditRequest, seasonToEdit);

		// then
		verify(seasonEditService).validateSeasonName(seasonEditRequest.getSeasonName());

	}

	@Test
	void should_ChangeSeasonName_WhenSeasonNameNotUsed() throws Exception {
		// given
		doNothing().when(seasonEditService).validateSeasonName(anyString());

		Season seasonToEdit = new Season();
		SeasonEditRequest seasonEditRequest = new SeasonEditRequest(BigInteger.ONE, "Edited Season Name",
				"01/01/3000");

		given(seasonRepository.findByNameIgnoreCase(anyString())).willReturn(null);

		// when
		seasonEditService.changeSeasonName(seasonEditRequest, seasonToEdit);

		// then
		assertEquals("Edited Season Name", seasonToEdit.getName());

	}

	@Test
	void should_NotChangeSeasonName_WhenSeasonNameAlreadyUsed() throws Exception {
		// given
		doNothing().when(seasonEditService).validateSeasonName(anyString());

		Season seasonToEdit = new Season();
		SeasonEditRequest seasonEditRequest = new SeasonEditRequest(BigInteger.ONE, "Edited Season Name",
				"01/01/3000");

		given(seasonRepository.findByNameIgnoreCase(anyString())).willReturn(new Season());

		// when
		Throwable exception = assertThrows(SeasonService.SeasonException.class, () -> {
			seasonEditService.changeSeasonName(seasonEditRequest, seasonToEdit);
		});

		// then
		assertThat(exception.getMessage(), containsString("season name you provided already exists."));

	}

}