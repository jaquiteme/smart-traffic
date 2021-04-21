Vue.component('toast-item', {
    props: ['title', 'type', 'time', 'message'],
    template: `
    <div
          class="toast"
          role="alert"
          aria-live="assertive"
          aria-atomic="true"
        >
          <div :class="type">
            <strong class="me-auto"> {{ title }} </strong>
            <small> {{ time }} </small>
          </div>
          <div class="toast-body"> {{ message }}</div>
        </div>
  `
});