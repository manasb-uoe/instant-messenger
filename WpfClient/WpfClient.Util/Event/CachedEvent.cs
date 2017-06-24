using System;
using System.Collections.Generic;
using Prism.Events;
using WpfClient.Model;

namespace WpfClient.Util.Event
{
    public class CachedEvent<T> : PubSubEvent<T>
    {
        private readonly List<T> cache = new List<T>();

        private bool HasSubscribers => Subscriptions.Count > 0;

        public override void Publish(T payload)
        {
            cache.Add(payload);
            Flush();
        }

        public override SubscriptionToken Subscribe(Action<T> action, ThreadOption threadOption,
            bool keepSubscriberReferenceAlive, Predicate<T> filter)
        {
            var token = base.Subscribe(action, threadOption, keepSubscriberReferenceAlive, filter);
            Flush();
            return token;
        }

        private void Flush()
        {
            if (!HasSubscribers)
            {
                return;
            }

            foreach (var payload in cache) 
            {
                base.Publish(payload);
            }

            cache.Clear();
        }
    }
}