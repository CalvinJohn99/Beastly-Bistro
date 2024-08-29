package com.csse3200.game.components.player;

import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.*;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.events.EventHandler;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;

@ExtendWith(GameExtension.class)
class KeyboardPlayerInputComponentTest {

  private KeyboardPlayerInputComponent inputComponent;
  private Entity entityMock;
  private EventHandler eventHandlerMock;

  @BeforeEach
  void setUp() {
    entityMock = mock(Entity.class);
    eventHandlerMock = mock(EventHandler.class);
    when(entityMock.getEvents()).thenReturn(eventHandlerMock);

    inputComponent = new KeyboardPlayerInputComponent();
    inputComponent.setEntity(entityMock);
  }

  @Test
  void shouldTriggerInteract() {
    inputComponent.keyDown(Keys.E);
    verify(eventHandlerMock, times(1)).trigger("interact");
  }

  @Test
  void shouldTriggerWalkUp() {
      inputComponent.keyDown(Keys.W);
      verify(eventHandlerMock, times(1)).trigger("walk", new Vector2(0, 1));
  }

  @Test
  void shouldTriggerWalkLeft() {
      inputComponent.keyDown(Keys.A);
      verify(eventHandlerMock, times(1)).trigger("walk", new Vector2(-1.0f, 0.0f));
  }

  @Test
  void shouldTriggerWalkDown() {
      inputComponent.keyDown(Keys.S);
      verify(eventHandlerMock, times(1)).trigger("walk", new Vector2(0.0f, -1.0f));
  }

  @Test
  void shouldTriggerWalkRight() {
      inputComponent.keyDown(Keys.D);
      verify(eventHandlerMock, times(1)).trigger("walk", new Vector2(1.0f, 0.0f));
  }

  @Test
  void shouldStopOnButtonRelease() {
    inputComponent.keyDown(Keys.W);
    inputComponent.keyUp(Keys.W);
    verify(eventHandlerMock).trigger("walkStop");

    inputComponent.keyDown(Keys.A);
    inputComponent.keyUp(Keys.A);
    verify(eventHandlerMock, times(2)).trigger("walkStop");
  }

}