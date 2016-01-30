package com.karumi.katasuperheroes;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.karumi.katasuperheroes.di.MainComponent;
import com.karumi.katasuperheroes.di.MainModule;
import com.karumi.katasuperheroes.model.SuperHero;
import com.karumi.katasuperheroes.model.SuperHeroesRepository;
import com.karumi.katasuperheroes.ui.view.SuperHeroDetailActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import it.cosenonjaviste.daggermock.DaggerMockRule;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.karumi.katasuperheroes.matchers.ToolbarMatcher.onToolbarWithTitle;
import static org.hamcrest.Matchers.allOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by firezenk on 30/1/16.
 */
@RunWith(AndroidJUnit4.class) @LargeTest public class DetailActivityTest {

    @Rule public DaggerMockRule<MainComponent> daggerRule =
            new DaggerMockRule<>(MainComponent.class, new MainModule()).set(
                    new DaggerMockRule.ComponentSetter<MainComponent>() {
                        @Override public void setComponent(MainComponent component) {
                            SuperHeroesApplication app =
                                    (SuperHeroesApplication) InstrumentationRegistry.getInstrumentation()
                                            .getTargetContext()
                                            .getApplicationContext();
                            app.setComponent(component);
                        }
                    });

    @Rule public IntentsTestRule<SuperHeroDetailActivity> activityRule =
            new IntentsTestRule<>(SuperHeroDetailActivity.class, true, false);

    @Mock SuperHeroesRepository repository;

    @Test public void toolbarShowsSuperHeroName() {
        SuperHero superHero = createOneSuperHero();

        startActivity(superHero);

        onToolbarWithTitle(superHero.getName()).check(matches(isDisplayed()));
    }

    @Test public void screenShowsSuperHeroName() {
        SuperHero superHero = createOneSuperHero();

        startActivity(superHero);

        onView(allOf(withId(R.id.tv_super_hero_name), withText(superHero.getName()))).check(matches(isDisplayed()));
    }

    @Test public void screenShowsAvengersBadge() {
        SuperHero superHero = createOneSuperHero();

        startActivity(superHero);

        onView(withId(R.id.iv_avengers_badge)).check(matches(isDisplayed()));
    }

    private SuperHero createOneSuperHero() {
        SuperHero superHero = new SuperHero("Scarlet Witch",
                "https://i.annihil.us/u/prod/marvel/i/mg/9/b0/537bc2375dfb9.jpg", true,
                "Scarlet Witch was born at the Wundagore base of the High Evolutionary, she and her twin "
                        + "brother Pietro were the children of Romani couple Django and Marya Maximoff. The "
                        + "High Evolutionary supposedly abducted the twins when they were babies and "
                        + "experimented on them, once he was disgusted with the results, he returned them to"
                        + " Wundagore, disguised as regular mutants.");
        when(repository.getByName(superHero.getName())).thenReturn(superHero);
        return superHero;
    }

    private SuperHeroDetailActivity startActivity(SuperHero superHero) {
        Intent intent = new Intent();
        intent.putExtra("super_hero_name_key", superHero.getName());
        return activityRule.launchActivity(intent);
    }
}
