package com.seasongg.season.services;

import com.seasongg.game.models.Game;
import com.seasongg.season.models.Season;
import com.seasongg.season.models.SeasonCreateRequest;
import com.seasongg.user.models.permissions.Permission;
import com.seasongg.user.services.permissions.PermissionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SeasonDeleteServiceTest {

	@Spy
	@InjectMocks
	private SeasonDeleteService seasonDeleteService;

	@Mock
	SeasonRepository seasonRepository;

	@Mock
	PermissionRepository permissionRepository;

	@Test
	void should_DeleteSeason_When_InputOK() throws Exception {
		// given
		doReturn(mock(Season.class)).when(seasonDeleteService).findSeason(BigInteger.ONE);

		doNothing().when(seasonDeleteService).validateSeasonId(BigInteger.ONE);

		doNothing().when(seasonRepository).delete(any(Season.class));

		doNothing().when(seasonDeleteService).deleteAllPermissionsForSeason(BigInteger.ONE);

		// when
		seasonDeleteService.deleteSeason(BigInteger.ONE);

		// then
		verify(seasonRepository).delete(any(Season.class));

	}

	@Test
	void should_ThrowException_When_NoSeasonFound() throws SeasonService.SeasonException {
		// given
		doNothing().when(seasonDeleteService).validateSeasonId(BigInteger.valueOf(-1));

		given(seasonRepository.findById(BigInteger.valueOf(-1))).willReturn(Optional.empty());

		// when
		Executable executable = () -> seasonDeleteService.deleteSeason(BigInteger.valueOf(-1));

		// then
		assertThrows(SeasonService.SeasonException.class, executable);

	}

	@Test
	void should_DeletePermissions_When_SeasonDeleted() throws Exception {
		// given
		List<Permission> dummyPermissionsToDelete = Arrays.asList(new Permission());

		given(permissionRepository.getAllPermissionsForSeason(BigInteger.ONE)).willReturn(dummyPermissionsToDelete);

		// when
		seasonDeleteService.deleteAllPermissionsForSeason(BigInteger.ONE);

		// then
		verify(permissionRepository).deleteAll(dummyPermissionsToDelete);

	}

}