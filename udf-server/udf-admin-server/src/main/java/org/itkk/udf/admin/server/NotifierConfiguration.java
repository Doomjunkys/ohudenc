package org.itkk.udf.admin.server;

import de.codecentric.boot.admin.notify.MailNotifier;
import de.codecentric.boot.admin.notify.Notifier;
import de.codecentric.boot.admin.notify.RemindingNotifier;
import de.codecentric.boot.admin.notify.filter.FilteringNotifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.concurrent.TimeUnit;

/**
 * 通知
 */
@Configuration
@ConditionalOnBean(MailNotifier.class)
@AutoConfigureAfter(de.codecentric.boot.admin.config.NotifierConfiguration.MailNotifierConfiguration.class)
public class NotifierConfiguration {

    /**
     * delegate
     */
    @Autowired
    private Notifier delegate;

    /**
     * filteringNotifier
     *
     * @return FilteringNotifier
     */
    @Bean
    public FilteringNotifier filteringNotifier() {
        return new FilteringNotifier(delegate);
    }

    /**
     * remindingNotifier
     *
     * @return RemindingNotifier
     */
    @Bean
    @Primary
    public RemindingNotifier remindingNotifier() {
        final int duration = 10;
        RemindingNotifier notifier = new RemindingNotifier(filteringNotifier());
        notifier.setReminderPeriod(TimeUnit.SECONDS.toMillis(duration));
        return notifier;
    }

    /**
     * remind
     */
    @Scheduled(fixedRate = 60_000L)
    public void remind() {
        remindingNotifier().sendReminders();
    }

}
